package repair.regen.processor.refinement_checker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.awt.Container;
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
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.InvocationPredicate;
import repair.regen.processor.constraints.LiteralPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.GhostFunction;
import repair.regen.processor.context.GhostState;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Utils;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import repair.regen.utils.ErrorPrinter;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.reference.CtExecutableReferenceImpl;
import spoon.support.sniper.internal.ElementSourceFragment;

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
		}else {
			setDefaultState(f, tc);
		}

	}


	public static void setDefaultState(RefinedFunction f, TypeChecker tc) {
		String[] path = f.getTargetClass().split("\\.");
		String klass = path[path.length-1];

		String[] s = {tc.THIS};
		Constraint c = new Predicate();
		List<GhostFunction> sets = getDifferentSets(tc, klass);
		for(GhostFunction sg: sets) {
			if(sg.getReturnType().toString().equals("int")) {
				Predicate p = new EqualsPredicate(
						new InvocationPredicate(sg.getName(), s),
						LiteralPredicate.getIntPredicate(0));
				c = Conjunction.createConjunction(c, p);
			}else {
				fail("Ghost Functions not implemented for other types than int -> implement in AuxStateHandler defaultState");
			}	
		}
		ObjectState os = new ObjectState();
		os.setTo(c);
		List<ObjectState> los = new ArrayList<>();
		los.add(os);
		f.setAllStates(los);
		//		System.out.println();
	}

	private static List<GhostFunction> getDifferentSets(TypeChecker tc, String klass) {
		List<GhostFunction> sets = new ArrayList<>();
		List<GhostState> l = tc.context.getGhostState(klass);
		if(l != null)
			for(GhostState g: l) {
				if(g.getParent() == null) {
					sets.add(g);
				} else if(!sets.contains(g.getParent()))
					sets.add(g.getParent());

			}
		return sets;
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
			l.add(getStates(an, f, tc, element));
		}
		f.setAllStates(l);
	}

	private static ObjectState getStates(CtAnnotation<? extends Annotation> ctAnnotation, 
			RefinedFunction f, TypeChecker tc, CtElement e) {
		Map<String, CtExpression> m = ctAnnotation.getAllValues();
		CtLiteral<String> from = (CtLiteral<String>)m.get("from");
		CtLiteral<String> to = (CtLiteral<String>)m.get("to");
		ObjectState state = new ObjectState();
		if(from != null)				//has From
			state.setFrom(createStateConstraint(from.getValue(), f, tc, e, false));
		if(to != null)					//has To
			state.setTo(createStateConstraint(to.getValue(), f, tc, e, true));

		if(from != null && to == null)	//has From but not To -> the state remains the same 
			state.setTo(createStateConstraint(from.getValue(), f, tc, e, true));
		if(from == null && to != null)	//has To but not From -> enters with true and exists with a specific state
			state.setFrom(new Predicate());
		return state;
	}


	private static Constraint createStateConstraint(String value, RefinedFunction f, TypeChecker tc, CtElement e, boolean isTo) {
		Predicate p = new Predicate(value);
		String t = f.getTargetClass();
		CtTypeReference<?> r = tc.factory.Type().createReference(t);

		String nameOld = String.format(tc.instanceFormat, tc.THIS, tc.context.getCounter());
		String name = String.format(tc.instanceFormat, tc.THIS, tc.context.getCounter());
		tc.context.addVarToContext(name, r, new Predicate());
		tc.context.addVarToContext(nameOld, r, new Predicate());

		Constraint c1 = isTo? getMissingStates(t, tc, p): p;
		Constraint c = c1.substituteVariable(tc.THIS, name);
		c = c.changeOldMentions(nameOld, "");
		boolean b = tc.checkStateSMT(new Predicate(), c.negate(), e);
		if(b) ErrorPrinter.printSameStateSetError(e, p, t);	

		return c1;



	}

	private static Constraint getMissingStates(String t, TypeChecker tc, Predicate p) {
		String[] temp= t.split("\\.");
		String simpleT = temp[temp.length-1];
		List<GhostState> gs = p.getGhostInvocations(tc.context.getGhostState(simpleT));
		List<GhostFunction> sets = getDifferentSets(tc, simpleT);
		for(GhostState g: gs) {
			if(g.getParent() == null && sets.contains(g))
				sets.remove(g);
			else if(g.getParent() != null && sets.contains(g.getParent()))
				sets.remove(g.getParent());
		}
		return addOldStates(p, tc.THIS, sets);
	}


	private static Constraint addOldStates(Predicate p, String th, List<GhostFunction> sets) {
		Constraint c = p;
		for(GhostFunction gf: sets) {
			Constraint eq = new EqualsPredicate(
					new InvocationPredicate(gf.getName(), th),
					new InvocationPredicate(gf.getName(), 
							new InvocationPredicate("old", th).getExpression()));
			c = Conjunction.createConjunction(c, eq);
		}
		return c;
	}


	//################ Handling State Change effects ################

	/**
	 * Sets the new state acquired from the constructor call
	 * @param key
	 * @param f
	 * @param map 
	 * @param ctConstructorCall
	 */
	public static void constructorStateMetadata(String refKey, RefinedFunction f, Map<String, String> map, CtConstructorCall<?> ctConstructorCall) {
		List<Constraint> oc = f.getToStates();
		if(oc.size() > 0 ){//&& !oc.get(0).isBooleanTrue())
			//			ctConstructorCall.putMetadata(stateKey, oc.get(0));
			Constraint c = oc.get(0);
			for(String k:map.keySet()) {
				c = c.substituteVariable(k, map.get(k));
			}
			ctConstructorCall.putMetadata(refKey, c);
			//add maping to oc.get(0)-HERE
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
	 * @param target2 
	 * @param target2 
	 * @param map 
	 * @param invocation
	 */
	public static void checkTargetChanges(TypeChecker tc, RefinedFunction f, CtExpression<?> target2, Map<String, String> map, CtElement invocation) {
		String parentTargetName = searchFistVariableTarget(tc, target2, invocation);
		VariableInstance target = getTarget(tc, invocation);
		if(target != null) {
			Constraint ref = new Predicate();
			if(f.hasStateChange() && f.getFromStates().size()>0)
				ref = changeState(tc, target, f, parentTargetName, map, invocation);
			if(!f.hasStateChange()) 
				ref = sameState(tc, target, parentTargetName, invocation);

			System.out.println();
		}

	}



	/**
	 * Changes the state
	 * @param tc
	 * @param vi
	 * @param f
	 * @param name
	 * @param map 
	 * @param invocation
	 * @return
	 */
	private static Constraint changeState(TypeChecker tc, VariableInstance vi, RefinedFunction f, String name,
			Map<String, String> map, CtElement invocation) {
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
				Constraint prevCheck = prevState;
				for(String s : map.keySet()) {
					prevCheck = prevCheck.substituteVariable(s, map.get(s));
					expectState = expectState.substituteVariable(s, map.get(s));
				}
				expectState = expectState.changeOldMentions(vi.getName(), instanceName);
				
				found = tc.checkStateSMT(prevCheck, expectState, invocation);
				if(found && os.hasTo()) {
					String newInstanceName = String.format(tc.instanceFormat, name, tc.context.getCounter()); 
					Constraint transitionedState = os.getTo()
							.substituteVariable(tc.WILD_VAR, newInstanceName)
							.substituteVariable(tc.THIS, newInstanceName);
					for(String s : map.keySet()) {
						transitionedState = transitionedState.substituteVariable(s, map.get(s));
					}
					transitionedState = checkOldMentions(transitionedState, instanceName, newInstanceName);
					addInstanceWithState(tc, name, newInstanceName, vi, transitionedState, invocation);
					return transitionedState;

				}
			}
		}
		if(!found) {//Reaches the end of stateChange no matching states
			String states = los.stream().filter(p->p.hasFrom())
					.map(p->p.getFrom().toString())
					.collect(Collectors.joining(","));
			String simpleInvocation = f.getName();
			if(invocation instanceof CtInvocation) {
				CtInvocation i = (CtInvocation) invocation;
				simpleInvocation = i.getExecutable().toString();
				System.out.println();
			}
				
			ErrorPrinter.printStateMismatch(invocation, simpleInvocation, prevState, states);
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

			addInstanceWithState(tc, name, newInstanceName, variableInstance, c, invocation);
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
	 * @param invocation 
	 * @return
	 */
	private static String addInstanceWithState(TypeChecker tc, String superName, 
			String name2, VariableInstance prevInstance, Constraint transitionedState, CtElement invocation) {
		VariableInstance vi2 = (VariableInstance)tc.context.addInstanceToContext( 
				name2, prevInstance.getType() , prevInstance.getRefinement());
		//		vi2.setState(transitionedState);
		vi2.setRefinement(transitionedState);
		RefinedVariable rv = tc.context.getVariableByName(superName);
		for(CtTypeReference<?> t: rv.getSuperTypes())
			vi2.addSuperType(t);
		
		Constraint superC = rv.getMainRefinement().substituteVariable(rv.getName(), vi2.getName());
		tc.checkSMT(superC, invocation);
		//verify super main refinement 
		tc.context.addRefinementInstanceToVariable(superName, name2);
		invocation.putMetadata(tc.TARGET_KEY, vi2);
		return name2;
	}


	/**
	 * Gets the name of the parent target and adds the 
	 * closest target to the elem TARGET metadata
	 * @param invocation
	 * @return
	 */
	static String searchFistVariableTarget(TypeChecker tc, CtElement elem, CtElement invocation) {
		if(elem instanceof CtVariableRead<?>) {
			CtVariableRead<?> v = (CtVariableRead<?>)elem;
			String name = v.getVariable().getSimpleName();
			Optional<VariableInstance> ovi = tc.context.getLastVariableInstance(name);
			if(ovi.isPresent())
				invocation.putMetadata(tc.TARGET_KEY, ovi.get());
			else if( elem.getMetadata(tc.TARGET_KEY) == null) {
				RefinedVariable var = tc.context.getVariableByName(name);
				String nName = String.format(tc.instanceFormat, name, tc.context.getCounter());
				RefinedVariable rv = tc.context.addInstanceToContext(nName, var.getType(), 
						var.getRefinement().substituteVariable(name, nName));
				tc.context.addRefinementInstanceToVariable(name, nName);
				invocation.putMetadata(tc.TARGET_KEY, rv);
			}
				
			return name;
		}else if(elem.getMetadata(tc.TARGET_KEY) != null) {
			VariableInstance vi = (VariableInstance)elem.getMetadata(tc.TARGET_KEY);
			Optional<Variable> v = vi.getParent();
			invocation.putMetadata(tc.TARGET_KEY, vi);
			return v.isPresent()? v.get().getName() : vi.getName();

		}
		return null;
	}

	static VariableInstance getTarget(TypeChecker tc, CtElement invocation) {
		if(invocation.getMetadata(tc.TARGET_KEY)!=null)
			return (VariableInstance)invocation.getMetadata(tc.TARGET_KEY);
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
