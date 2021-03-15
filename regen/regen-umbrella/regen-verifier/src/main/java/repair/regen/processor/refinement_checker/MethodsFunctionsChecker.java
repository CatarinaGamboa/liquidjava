package repair.regen.processor.refinement_checker;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.ObjectState;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtExecutableReference;

public class MethodsFunctionsChecker {

	private TypeChecker rtc;

	private static String retNameFormat = "#ret_%d";

	public MethodsFunctionsChecker(TypeChecker rtc) {
		this.rtc = rtc; 

	}

	public void getConstructorRefinements(CtConstructor<?> c) {	
		RefinedFunction f = new RefinedFunction();
		f.setName(c.getSimpleName());
		f.setType(c.getType());
		f.setRefReturn(new Predicate());
		if(c.getParent() instanceof CtClass) {
			CtClass klass = (CtClass)c.getParent();
			f.setClass(klass.getQualifiedName());
		}
		rtc.context.addFunctionToContext(f);
		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(c);
		if(!an.isEmpty()) {
			for(CtAnnotation<? extends Annotation> a: an) {
				Map<String, CtExpression> m = a.getAllValues();
				CtLiteral<String> from = (CtLiteral<String>)m.get("from");
				if(from != null)
					ErrorPrinter.printErrorConstructorFromState(c, from);
			}
			f.setState(an, rtc.context.getGhosts(), c);
		}
	}


	public void getConstructorInvocationRefinements(CtConstructorCall<?> ctConstructorCall) {
		CtExecutableReference<?> exe = ctConstructorCall.getExecutable();
		if(exe != null) {
			RefinedFunction f = rtc.context.getFunction(exe.getSimpleName(), 
					exe.getDeclaringType().getQualifiedName());
			if(f != null) {
				List<Constraint> oc = f.getToStates();
				if(oc.size() > 0 )//&& !oc.get(0).isBooleanTrue())
					ctConstructorCall.putMetadata(rtc.STATE_KEY, oc.get(0));
				else if(oc.size() > 1)
					assertFalse("Constructor can only have one to state, not multiple", true);
			}
		}

	}


	//################### VISIT METHOD ##############################
	<R> void getMethodRefinements(CtMethod<R> method) {
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn(new Predicate());

		CtClass klass = null;
		if(method.getParent() instanceof CtClass) {
			klass = (CtClass)method.getParent();
			f.setClass(klass.getQualifiedName());
		}
		if(method.getParent() instanceof CtInterface<?>) {
			CtInterface<?> inter = (CtInterface<?>)method.getParent();
			f.setClass(inter.getQualifiedName());
		}
		rtc.context.addFunctionToContext(f);
		auxGetMethodRefinements(method, f);

		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(method);
		if(!an.isEmpty())
			f.setState(an, rtc.context.getGhosts(), method);
		
		if(klass != null)
			AuxHierarchyRefinememtsPassage.checkFunctionInSupertypes(klass, method, f, rtc);
	}




	<R> void getMethodRefinements(CtMethod<R> method, String prefix) {
		String[] pac = prefix.split("\\.");
		String k = pac[pac.length-1]; 

		String functionName = String.format("%s.%s", prefix, method.getSimpleName());
		if(k.equals(method.getSimpleName())) {//is a constructor
			functionName = String.format("<init>");
		}

		RefinedFunction f = new RefinedFunction();
		f.setName(functionName);
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
		f.setClass(prefix);
		rtc.context.addFunctionToContext(f);
		auxGetMethodRefinements(method, f);

		List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(method);
		if(!an.isEmpty())
			f.setState(an, rtc.context.getGhosts(), method);
	}

	private <R> void auxGetMethodRefinements(CtMethod<R> method, RefinedFunction rf) {
		//main cannot have refinement - for now
		if(method.getSignature().equals("main(java.lang.String[])"))
			return;	
		List<CtParameter<?>> params = method.getParameters();
		Constraint ref = handleFunctionRefinements(rf, method, params);
		method.putMetadata(rtc.REFINE_KEY, ref);

	}

	/**
	 * Joins all the refinements from parameters and return
	 * @param f
	 * @param methodRef
	 * @param params
	 * @return Conjunction of all 
	 */
	private Constraint handleFunctionRefinements(RefinedFunction f, CtMethod<?> method, 
			List<CtParameter<?>> params) {
		Constraint joint = new Predicate();

		for(CtParameter<?> param:params) {
			String paramName = param.getSimpleName();
			Optional<Constraint> oc = rtc.getRefinementFromAnnotation(param);
			Constraint c = new Predicate();
			if(oc.isPresent()) 
				c = oc.get().substituteVariable(rtc.WILD_VAR, paramName);
			param.putMetadata(rtc.REFINE_KEY, c);
			RefinedVariable v = rtc.context.addVarToContext(param.getSimpleName(), param.getType(), c);
			if(v instanceof Variable)
				f.addArgRefinements((Variable)v);
			joint = Conjunction.createConjunction(joint, c);
		}

		Optional<Constraint> oret = rtc.getRefinementFromAnnotation(method);
		Constraint ret = oret.isPresent()?oret.get():new Predicate();
		f.setRefReturn(ret);
		rtc.context.addFunctionToContext(f);
		return Conjunction.createConjunction(joint, ret);
	}

	public List<CtAnnotation<? extends Annotation>> getStateAnnotation(CtElement element) {
		List<CtAnnotation<? extends Annotation>> l = new ArrayList();
		for(CtAnnotation<? extends Annotation> ann :element.getAnnotations()) { 
			String an = ann.getActualAnnotation().annotationType().getCanonicalName();
			if( an.contentEquals("repair.regen.specification.StateRefinement")) {
				l.add(ann);
			}
		}
		return l;
	}




	<R> void getReturnRefinements(CtReturn<R> ret) {
		if(ret.getReturnedExpression() != null) {
			//check if there are refinements
			if(rtc.getRefinement(ret.getReturnedExpression())== null)
				ret.getReturnedExpression().putMetadata(rtc.REFINE_KEY, new Predicate());
			CtMethod method = ret.getParent(CtMethod.class);
			//check if method has refinements
			if(rtc.getRefinement(method) == null)
				return;
			if (method.getParent() instanceof CtClass) {
				RefinedFunction fi = rtc.context.getFunction(method.getSimpleName(), 
						((CtClass)method.getParent()).getQualifiedName());

				//Both return and the method have metadata
				String returnVarName = String.format(retNameFormat,rtc.context.getCounter());
				Constraint cretRef = rtc.getRefinement(ret.getReturnedExpression()).substituteVariable(rtc.WILD_VAR, returnVarName);
				Constraint cexpectedType = fi.getRefReturn().substituteVariable(rtc.WILD_VAR, returnVarName);

				RefinedVariable rv = rtc.context.addVarToContext(returnVarName, method.getType(), cretRef);
				rtc.checkSMT(cexpectedType, ret);
				rtc.context.newRefinementToVariableInContext(returnVarName, cexpectedType);
			}
		}
	}


	//############################### VISIT INVOCATION ################################3

	<R> void getInvocationRefinements(CtInvocation<R> invocation) {
		CtExecutable<?> method = invocation.getExecutable().getDeclaration();
		if(method == null) {
			Method m = invocation.getExecutable().getActualMethod();
			if(m != null) searchMethodInLibrary(m, invocation);

		}else if(method.getParent() instanceof CtClass){
			String ctype = ((CtClass)method.getParent()).getQualifiedName();
			RefinedFunction f = rtc.context.getFunction(method.getSimpleName(), ctype);
			if(f != null) {//inside rtc.context
				checkInvocationRefinements(invocation, method.getSimpleName(), ctype);

			}else {
				CtExecutable cet = invocation.getExecutable().getDeclaration();
				if(cet instanceof CtMethod) {
					//					CtMethod met = (CtMethod) cet;
					//					rtc.visitCtMethod(met);
					//					checkInvocationRefinements(invocation, method.getSimpleName());
				}
				//				rtc.visitCtMethod(method);

			}
		}
	}





	private void checkTargetChanges(CtExpression<?> target, RefinedFunction f, CtInvocation<?> invocation) {		
		if(target instanceof CtVariableRead<?>) {
			CtVariableRead<?> v = (CtVariableRead<?>)target;
			String name = v.getVariable().getSimpleName();
			Optional<VariableInstance> ovi = rtc.context.getLastVariableInstance(name);
			Constraint ref = new Predicate();
			if(ovi.isPresent() && f.hasStateChange() && f.getFromStates().size()>0)
				ref = changeState(ovi.get(), f, name, invocation);
			if(ovi.isPresent() && !f.hasStateChange()) 
				ref = sameState(ovi.get(), name, invocation);

			invocation.putMetadata(rtc.STATE_KEY, ref);

		}

	}

	private Constraint sameState(VariableInstance variableInstance, String name, CtInvocation<?> invocation) {
		Constraint c = variableInstance.getState();
		addInstanceWithState(name, variableInstance, c);
		return c;
	}

	private Constraint changeState(VariableInstance vi, RefinedFunction f, String name, CtInvocation<?> invocation) {
		Constraint prevState = vi.getState();
		List<ObjectState> los = f.getAllStates();
		boolean found = false;
		for (int i = 0; i < los.size() && !found; i++) {
			ObjectState os = los.get(i);
			if(os.hasFrom()) {
				Constraint expectState = os.getFrom().substituteVariable(rtc.THIS, name); 
				found = rtc.checkStateSMT(prevState, expectState, invocation);
				if(found && os.hasTo()) {
						Constraint transitionedState = os.getTo().substituteVariable(rtc.THIS, name);
						addInstanceWithState(name, vi, transitionedState);
						return transitionedState;
					
				}
			}
		}
		if(!found) {
			System.out.println("Reached end of changeState with found false");
			String states = los.stream().filter(p->p.hasFrom())
										.map(p->p.getFrom().toString())
										.collect(Collectors.joining(","));
			ErrorPrinter.printStateMismatch(invocation, prevState, states);
		}
		return new Predicate();
	}

	private void addInstanceWithState(String superName, VariableInstance prevInstance, Constraint transitionedState) {
		String name2 = String.format(rtc.instanceFormat, superName, rtc.context.getCounter()); 
		VariableInstance vi2 = (VariableInstance)rtc.context.addInstanceToContext( 
				name2, prevInstance.getType() , prevInstance.getRefinement());
		vi2.setState(transitionedState);
		rtc.context.addRefinementInstanceToVariable(superName, name2);
	}

	private void searchMethodInLibrary(Method m, CtInvocation<?> invocation) {
		String ctype = m.getDeclaringClass().getCanonicalName();
		if(rtc.context.getFunction(m.getName(),ctype) != null) {//inside rtc.context
			checkInvocationRefinements(invocation, m.getName(), ctype);
			return;
		}else {
			String name = m.getName();
			String prefix = m.getDeclaringClass().getCanonicalName();
			String completeName = String.format("%s.%s", prefix, name);
			if(rtc.context.getFunction(completeName, ctype) != null) {
				checkInvocationRefinements(invocation, completeName, ctype);
			}

		}

	}

	private <R> void checkInvocationRefinements(CtInvocation<R> invocation, String methodName, String className) {
		//		invocation.getTarget().getType().toString()
		int si = invocation.getArguments().size();
		RefinedFunction f = rtc.context.getFunction(methodName, className, si);
		if(invocation.getTarget() != null) {
			checkTargetChanges(invocation.getTarget(), f, invocation);
		}

		if(f.allRefinementsTrue()) {
			invocation.putMetadata(rtc.REFINE_KEY, new Predicate());
			return;
		}
		Map<String,String> map = mapInvocation(invocation, f);

		checkParameters(invocation, f, map);

		Constraint methodRef = f.getRefReturn(); 
		if(methodRef != null) {
			List<String> vars = methodRef.getVariableNames(); 
			for(String s:vars) 
				if(map.containsKey(s))
					methodRef = methodRef.substituteVariable(s, map.get(s));
			invocation.putMetadata(rtc.REFINE_KEY, methodRef);
		}


	}

	private <R> Map<String, String> mapInvocation(CtInvocation<R> invocation, RefinedFunction f){
		Map<String, String> mapInvocation = new HashMap<>();
		List<CtExpression<?>> invocationParams = invocation.getArguments();
		List<Variable> functionParams = f.getArguments();
		for (int i = 0; i < invocationParams.size(); i++) {
			Variable fArg = functionParams.get(i);
			CtExpression<?> iArg = invocationParams.get(i);
			String invStr;
			//			if(iArg instanceof CtLiteral)
			//				invStr = iArg.toString();
			//			else 
			if(iArg instanceof CtFieldRead) {
				invStr = createVariableRepresentingArgument(iArg, fArg);
			}else if(iArg instanceof CtVariableRead) {
				CtVariableRead<?> vr = (CtVariableRead<?>)iArg;
				Optional<VariableInstance> ovi= rtc.context
						.getLastVariableInstance(vr.getVariable().getSimpleName());
				invStr = (ovi.isPresent())? ovi.get().getName() : vr.toString();
			}else //create new variable with the argument refinement
				invStr = createVariableRepresentingArgument(iArg, fArg);				


			mapInvocation.put(fArg.getName(), invStr);
		}
		return mapInvocation;
	}



	private String createVariableRepresentingArgument(CtExpression<?> iArg, Variable fArg) {
		Constraint met = (Constraint) iArg.getMetadata(rtc.REFINE_KEY);
		if(!met.getVariableNames().contains(rtc.WILD_VAR))
			met = new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), met);
		String nVar = String.format(rtc.instanceFormat, fArg.getName(),
				rtc.context.getCounter());
		rtc.context.addVarToContext(nVar, fArg.getType(), 
				met.substituteVariable(rtc.WILD_VAR, nVar));
		return nVar;
	}

	private <R> void checkParameters(CtInvocation<R> invocation, RefinedFunction f, Map<String, String> map) {	
		List<CtExpression<?>> invocationParams = invocation.getArguments();
		List<Variable> functionParams = f.getArguments();
		for (int i = 0; i < invocationParams.size(); i++) {
			Variable fArg = functionParams.get(i);
			Constraint c = fArg.getRefinement();
			c = c.substituteVariable(fArg.getName(), map.get(fArg.getName()));
			List<String> vars = c.getVariableNames();
			for(String s: vars)
				if(map.containsKey(s))
					c = c.substituteVariable(s, map.get(s));
			rtc.checkSMT(c, invocation);
		}

	}




}
