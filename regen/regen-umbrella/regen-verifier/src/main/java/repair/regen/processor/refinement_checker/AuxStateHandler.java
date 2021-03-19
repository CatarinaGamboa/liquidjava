package repair.regen.processor.refinement_checker;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Utils;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

public class AuxStateHandler {
	
	
	//########### Get State from StateRefinement declaration #############

	/**
	 * Handles the passage of the written state annotations to the context for Constructors
	 * @param c
	 * @param f
	 * @param context
	 */
	public static void handleConstructorState(CtConstructor<?> c, RefinedFunction f, TypeChecker tc) {
		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(c);
		if(!an.isEmpty()) {
			for(CtAnnotation<? extends Annotation> a: an) {
				Map<String, CtExpression> m = a.getAllValues();
				CtLiteral<String> from = (CtLiteral<String>)m.get("from");
				if(from != null)
					ErrorPrinter.printErrorConstructorFromState(c, from);
			}
			setFunctionStates(f, an, tc, c);//f.setState(an, context.getGhosts(), c);
		}
		
	}

	/**
	 * Handles the passage of the written state annotations to the context for regular Methods
	 * @param method
	 * @param context
	 * @param f
	 */
	public static void handleMethodState(CtMethod<?> method, RefinedFunction f, TypeChecker tc) {
		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(method);
		if(!an.isEmpty())
			setFunctionStates(f, an, tc, method);
//			f.setState(an, context.getGhosts(), method);
		
	}

	/**
	 * Creates the list of states and adds them to the function
	 * @param f
	 * @param anns
	 * @param tc
	 * @param element
	 */
	private static void setFunctionStates(RefinedFunction f, List<CtAnnotation<? extends Annotation>> anns,
			TypeChecker tc, CtElement element) {
		List<ObjectState> l = new ArrayList<>();
		for(CtAnnotation<? extends Annotation> an: anns) {
			l.add(getStates(an, tc, element));
		}
		f.setAllStates(l);
	}

	private static ObjectState getStates(CtAnnotation<? extends Annotation> ctAnnotation, 
										TypeChecker tc, CtElement e) {
		Map<String, CtExpression> m = ctAnnotation.getAllValues();
		CtLiteral<String> from = (CtLiteral<String>)m.get("from");
		CtLiteral<String> to = (CtLiteral<String>)m.get("to");
		ObjectState state = new ObjectState();
		if(from != null)				//has From
			state.setFrom(createStateConstraint(from.getValue(), tc, e));
		if(to != null)					//has To
			state.setTo(createStateConstraint(to.getValue(), tc, e));
		
		if(from != null && to == null)	//has From but not To -> the state remains the same 
			state.setTo(createStateConstraint(from.getValue(), tc, e));
		if(from == null && to != null)	//has To but not From -> enters with true and exists with a specific state
			state.setFrom(new Predicate());
		return state;
	}


	private static Constraint createStateConstraint(String value, TypeChecker tc, CtElement e) {
		Predicate p = new Predicate(value);
		List<GhostFunction> allGhosts = tc.context.getGhosts();
		List<GhostFunction> ghostsInAnnotation = p.getGhostInvocations(tc.context.getGhosts());
		Map<String,List<Integer>> referedSets = getReferedSets(ghostsInAnnotation);
		for(String k: referedSets.keySet()) {
			for(int i : referedSets.get(k)) {
				List<GhostFunction> allFromSet = getAllFromSet(allGhosts, k, i);
				String name = String.format(tc.instanceFormat, k, tc.context.getCounter());
				//should only have 1 param
				tc.context.addVarToContext(name, allFromSet.get(0).getParametersTypes().get(0), new Predicate());
				String[] ls = {name};
				Constraint disjoint = getAllPermutations(allFromSet, ls);
				Constraint c = p.substituteVariable(tc.THIS, name);
				boolean b = tc.checkStateSMT(disjoint, c.negate(), e);
				//If it is impossible then the check will return true, so in this case
				//we want to send an error because the states must be disjoint
				if(b) ErrorPrinter.printSameStateSetError(e, p, k);
			}
		}

		return p;
	}


	
	private static Constraint getAllPermutations(List<GhostFunction> allFromSet, String[] ls) {
		List<Constraint> l = allFromSet.stream().map(p->p.getInvocation(ls)).map(p -> new Predicate(p)).collect(Collectors.toList());
		Constraint c = new Predicate();
		for (int i = 0; i < l.size(); i++) {
			for (int j = i+1; j < l.size(); j++) {
				c = Conjunction.createConjunction(c, Conjunction.createConjunction(l.get(i), l.get(j)).negate());
			}
		}
		return c;
	}

	private static List<GhostFunction> getAllFromSet(List<GhostFunction> allGhosts, String k, int i) {
		List<GhostFunction> l = new ArrayList<>();
		for(GhostFunction g: allGhosts)
			if(g.getParentClassName().equals(k) && g.belongsToGroupSet() && g.getGroupSet() == i)
				l.add(g);
		return l;
	}

	private static Map<String, List<Integer>> getReferedSets(List<GhostFunction> ghostsInAnnotation) {
		Map<String,List<Integer>> differentSets = new HashMap<>();
		for(GhostFunction gf: ghostsInAnnotation) {
			if(gf.belongsToGroupSet()) {//belongs to a set state
				String name = gf.getParentClassName();
				if(!differentSets.containsKey(name))
					differentSets.put(name, new ArrayList());
				differentSets.get(name).add(gf.getGroupSet());
			}
		}
		return differentSets;
	}

	//################ Handling State Change effects ################

	/**
	 * Sets the new state acquired from the constructor call
	 * @param key
	 * @param f
	 * @param ctConstructorCall
	 */
	public static void constructorStateMetadata(String refKey, RefinedFunction f, CtConstructorCall<?> ctConstructorCall) {
		List<Constraint> oc = f.getToStates();
		if(oc.size() > 0 ){//&& !oc.get(0).isBooleanTrue())
//			ctConstructorCall.putMetadata(stateKey, oc.get(0));
			ctConstructorCall.putMetadata(refKey, oc.get(0));
		}else if(oc.size() > 1)
			assertFalse("Constructor can only have one to state, not multiple", true);
		
	}
	/**
	 * If an expression has a state in metadata, then its state is passed to the last instance
	 * of the variable with varName
	 * @param context
	 * @param state_key
	 * @param this_key
	 * @param varName
	 * @param e
	 */
	public static void addStateRefinements(TypeChecker tc, String varName, CtExpression<?> e) {
		Optional<VariableInstance> ovi = tc.context.getLastVariableInstance(varName);
		if(ovi.isPresent() && e.getMetadata(tc.REFINE_KEY) != null) {
			VariableInstance vi = ovi.get(); 
			Constraint c = (Constraint)e.getMetadata(tc.REFINE_KEY);
			c = c.substituteVariable(tc.THIS, vi.getName()).substituteVariable(tc.WILD_VAR, vi.getName());
			vi.setRefinement(c);
		}
	}

	/**
	 * Checks the changes in the state of the target
	 * @param tc
	 * @param f
	 * @param invocation
	 */
	public static void checkTargetChanges(TypeChecker tc, RefinedFunction f, CtElement invocation) {
		
		CtElement target = searchFistVariableTarget(invocation);
		if(target instanceof CtVariableRead<?>) {
			CtVariableRead<?> v = (CtVariableRead<?>)target;
			String name = v.getVariable().getSimpleName();
			Optional<VariableInstance> ovi = tc.context.getLastVariableInstance(name);
			Constraint ref = new Predicate();
			if(ovi.isPresent() && f.hasStateChange() && f.getFromStates().size()>0)
				ref = changeState(tc, ovi.get(), f, name, invocation);
			if(ovi.isPresent() && !f.hasStateChange()) 
				ref = sameState(tc, ovi.get(), name, invocation);

//			invocation.putMetadata(tc.REFINE_KEY, ref);

		}

	}


	
	/**
	 * Changes the state
	 * @param tc
	 * @param vi
	 * @param f
	 * @param name
	 * @param invocation
	 * @return
	 */
	private static Constraint changeState(TypeChecker tc, VariableInstance vi, RefinedFunction f, String name, CtElement invocation) {
		if(vi.getRefinement() == null)
			return new Predicate();
		String instanceName = vi.getName();
		Constraint prevState = vi.getRefinement()
					.substituteVariable(tc.WILD_VAR, instanceName)
					.substituteVariable(name, instanceName);
		List<ObjectState> los = f.getAllStates();
		
		boolean found = false;
//		if(los.size() > 1)
//			assertFalse("Change state only working for one methods with one state",true);
		for (int i = 0; i < los.size() && !found; i++) {//TODO: only working for 1 state annotation
			ObjectState os = los.get(i);
			if(os.hasFrom()) {
				Constraint expectState = os.getFrom().substituteVariable(tc.THIS, instanceName); 
				found = tc.checkStateSMT(prevState, expectState, invocation);
				if(found && os.hasTo()) {
					String newInstanceName = String.format(tc.instanceFormat, name, tc.context.getCounter()); 
					Constraint transitionedState = os.getTo()
							.substituteVariable(tc.WILD_VAR, newInstanceName)
							.substituteVariable(tc.THIS, newInstanceName);
					transitionedState = checkOldMentions(transitionedState, instanceName, newInstanceName);
					addInstanceWithState(tc, name, newInstanceName, vi, transitionedState);
					return transitionedState;
					
				}
			}
		}
		if(!found) {//Reaches the end of stateChange no matching states
			String states = los.stream().filter(p->p.hasFrom())
										.map(p->p.getFrom().toString())
										.collect(Collectors.joining(","));
			ErrorPrinter.printStateMismatch(invocation, prevState, states);
		}
		return new Predicate();
	}
	

	private static Constraint checkOldMentions(Constraint transitionedState, String instanceName,
			String newInstanceName) {
		return transitionedState.changeOldMentions(instanceName, newInstanceName);
	}

	/**
	 * Copies the previous state to the new variable instance
	 * @param tc
	 * @param variableInstance
	 * @param name
	 * @param invocation
	 * @return
	 */
	private static Constraint sameState(TypeChecker tc, VariableInstance variableInstance, String name, CtElement invocation) {
//		if(variableInstance.getState() != null) {
		if(variableInstance.getRefinement() != null) {
			String newInstanceName = String.format(tc.instanceFormat, name, tc.context.getCounter()); 
//			Constraint c = variableInstance.getState().substituteVariable(variableInstance.getName(), newInstanceName);
			Constraint c = variableInstance.getRefinement()
					.substituteVariable(tc.WILD_VAR, newInstanceName)
					.substituteVariable(variableInstance.getName(), newInstanceName);
			
			addInstanceWithState(tc, name, newInstanceName, variableInstance, c);
			return c;
		}
		return new Predicate();
	}


	/**
	 * Adds a new instance with the given state
	 * @param tc
	 * @param superName
	 * @param name2
	 * @param prevInstance
	 * @param transitionedState
	 * @return
	 */
	private static String addInstanceWithState(TypeChecker tc, String superName, 
			String name2, VariableInstance prevInstance, Constraint transitionedState) {
		VariableInstance vi2 = (VariableInstance)tc.context.addInstanceToContext( 
				name2, prevInstance.getType() , prevInstance.getRefinement());
//		vi2.setState(transitionedState);
		vi2.setRefinement(transitionedState);
		RefinedVariable rv = tc.context.getVariableByName(superName);
		for(CtTypeReference<?> t: rv.getSuperTypes())
			vi2.addSuperType(t);
		tc.context.addRefinementInstanceToVariable(superName, name2);
		return name2;
	}


	/**
	 * Gets the first target that is a CtVariable that appears in the invocation
	 * @param invocation
	 * @return
	 */
	static CtExpression<?> searchFistVariableTarget(CtElement invocation) {
		if(invocation instanceof CtInvocation<?>)
			return searchFistVariableTarget(((CtInvocation<?>)invocation).getTarget());
		else if(invocation instanceof CtVariableRead<?>)
			return (CtVariableRead<?>)invocation;
		return null;
	}


	private static List<CtAnnotation<? extends Annotation>> getStateAnnotation(CtElement element) {
		List<CtAnnotation<? extends Annotation>> l = new ArrayList();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			if( an.contentEquals("repair.regen.specification.StateRefinement")) {
				l.add(ann);
			}
		}
		return l;
	}

}
