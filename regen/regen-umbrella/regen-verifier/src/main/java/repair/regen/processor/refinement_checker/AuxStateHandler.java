package repair.regen.processor.refinement_checker;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
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

public class AuxStateHandler {

	/**
	 * Handles the passage of the written state annotations to the context for Constructors
	 * @param c
	 * @param f
	 * @param context
	 */
	public static void handleConstructorState(CtConstructor<?> c, RefinedFunction f, Context context) {
		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(c);
		if(!an.isEmpty()) {
			for(CtAnnotation<? extends Annotation> a: an) {
				Map<String, CtExpression> m = a.getAllValues();
				CtLiteral<String> from = (CtLiteral<String>)m.get("from");
				if(from != null)
					ErrorPrinter.printErrorConstructorFromState(c, from);
			}
			f.setState(an, context.getGhosts(), c);
		}
		
	}

	/**
	 * Handles the passage of the written state annotations to the context for regular Methods
	 * @param method
	 * @param context
	 * @param f
	 */
	public static void handleMethodState(CtMethod<?> method, Context context, RefinedFunction f) {
		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(method);
		if(!an.isEmpty())
			f.setState(an, context.getGhosts(), method);
		
	}
	

	
	/**
	 * Sets the new state acquired from the constructor call
	 * @param key
	 * @param f
	 * @param ctConstructorCall
	 */
	public static void constructorStateMetadata(String key, RefinedFunction f, CtConstructorCall<?> ctConstructorCall) {
		List<Constraint> oc = f.getToStates();
		if(oc.size() > 0 )//&& !oc.get(0).isBooleanTrue())
			ctConstructorCall.putMetadata(key, oc.get(0));
		else if(oc.size() > 1)
			assertFalse("Constructor can only have one to state, not multiple", true);
		
	}

	/**
	 * Checks the changes in the state of the target
	 * @param tc
	 * @param f
	 * @param invocation
	 */
	public static void checkTargetChanges(TypeChecker tc, RefinedFunction f, CtInvocation<?> invocation) {		
		CtElement target = searchFistTarget(invocation);
		if(target instanceof CtVariableRead<?>) {
			CtVariableRead<?> v = (CtVariableRead<?>)target;
			String name = v.getVariable().getSimpleName();
			Optional<VariableInstance> ovi = tc.context.getLastVariableInstance(name);
			Constraint ref = new Predicate();
			if(ovi.isPresent() && f.hasStateChange() && f.getFromStates().size()>0)
				ref = changeState(tc, ovi.get(), f, name, invocation);
			if(ovi.isPresent() && !f.hasStateChange()) 
				ref = sameState(tc, ovi.get(), name, invocation);

			invocation.putMetadata(tc.STATE_KEY, ref);

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
	private static Constraint changeState(TypeChecker tc, VariableInstance vi, RefinedFunction f, String name, CtInvocation<?> invocation) {
		if(vi.getState() == null)
			return new Predicate();
		String instanceName = vi.getName();
		Constraint prevState = vi.getState().substituteVariable(name, instanceName);
		List<ObjectState> los = f.getAllStates();
		boolean found = false;
		for (int i = 0; i < los.size() && !found; i++) {
			ObjectState os = los.get(i);
			if(os.hasFrom()) {
				Constraint expectState = os.getFrom().substituteVariable(tc.THIS, instanceName); 
				found = tc.checkStateSMT(prevState, expectState, invocation);
				if(found && os.hasTo()) {
						String newInstanceName = String.format(tc.instanceFormat, name, tc.context.getCounter()); 
						Constraint transitionedState = os.getTo().substituteVariable(tc.THIS, newInstanceName);
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
	

	/**
	 * Copies the previous state to the new variable instance
	 * @param tc
	 * @param variableInstance
	 * @param name
	 * @param invocation
	 * @return
	 */
	private static Constraint sameState(TypeChecker tc, VariableInstance variableInstance, String name, CtInvocation<?> invocation) {
		if(variableInstance.getState() != null) {
			String newInstanceName = String.format(tc.instanceFormat, name, tc.context.getCounter()); 
			Constraint c = variableInstance.getState().substituteVariable(variableInstance.getName(), newInstanceName);
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
	private static String addInstanceWithState(TypeChecker tc, String superName, String name2, VariableInstance prevInstance, Constraint transitionedState) {
		VariableInstance vi2 = (VariableInstance)tc.context.addInstanceToContext( 
				name2, prevInstance.getType() , prevInstance.getRefinement());
		vi2.setState(transitionedState);
		tc.context.addRefinementInstanceToVariable(superName, name2);
		return name2;
	}


	/**
	 * Gets the first target that is a CtVariable that appears in the invocation
	 * @param invocation
	 * @return
	 */
	static CtExpression searchFistTarget(CtInvocation<?> invocation) {
		if(invocation.getTarget() instanceof CtVariableRead<?>)
			return invocation.getTarget();
		else if(invocation.getTarget() instanceof CtInvocation)
			return searchFistTarget((CtInvocation)invocation.getTarget());
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
