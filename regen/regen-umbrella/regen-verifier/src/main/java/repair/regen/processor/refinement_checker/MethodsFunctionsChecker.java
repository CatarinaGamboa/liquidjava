package repair.regen.processor.refinement_checker;

import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VariablePredicate;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtInvocation;
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
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtVariableReference;

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
		handleFunctionRefinements(f, c, c.getParameters());
		f.setRefReturn(new Predicate());
		if(c.getParent() instanceof CtClass) {
			CtClass klass = (CtClass)c.getParent();
			f.setClass(klass.getQualifiedName());
		}
		rtc.context.addFunctionToContext(f);
		AuxStateHandler.handleConstructorState(c, f, rtc);
	}


	public void getConstructorInvocationRefinements(CtConstructorCall<?> ctConstructorCall) {
		CtExecutableReference<?> exe = ctConstructorCall.getExecutable();
		if(exe != null) {
			RefinedFunction f = rtc.context.getFunction(exe.getSimpleName(), 
					exe.getDeclaringType().getQualifiedName());
			if(f != null) {
				checkInvocationRefinements(ctConstructorCall, ctConstructorCall.getArguments(), ctConstructorCall.getTarget(), 
						f.getName(), f.getTargetClass());
				AuxStateHandler.constructorStateMetadata(rtc.REFINE_KEY, f, ctConstructorCall);
				
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
		
		long d1 = System.currentTimeMillis();
		auxGetMethodRefinements(method, f);
		long d2 = System.currentTimeMillis();
		System.out.println("auxGetMethod refinements:" + (d2-d1));
		
		d1 = System.currentTimeMillis();
		AuxStateHandler.handleMethodState(method, f, rtc);
		d2 = System.currentTimeMillis();
		System.out.println("handleState refinements:" + (d2-d1));
		
		if(klass != null)
			AuxHierarchyRefinememtsPassage.checkFunctionInSupertypes(klass, method, f, rtc);

		
	}




	<R> void getMethodRefinements(CtMethod<R> method, String prefix) {
		String constructorName = "<init>";
		String[] pac = prefix.split("\\.");
		String k = pac[pac.length-1]; 

		String functionName = String.format("%s.%s", prefix, method.getSimpleName());
		if(k.equals(method.getSimpleName())) {//is a constructor
			functionName = String.format(constructorName);
		}

		RefinedFunction f = new RefinedFunction();
		f.setName(functionName);
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
		f.setClass(prefix);
		rtc.context.addFunctionToContext(f);
		auxGetMethodRefinements(method, f);

		AuxStateHandler.handleMethodState(method, f, rtc);
		if(functionName.equals(constructorName) && !f.hasStateChange()) {
			AuxStateHandler.setDefaultState(f, rtc);
		}
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
	private Constraint handleFunctionRefinements(RefinedFunction f, CtElement method, 
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
//		rtc.context.addFunctionToContext(f);
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
		CtClass c = ret.getParent(CtClass.class);
		String className = c.getSimpleName();
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
				
				List<Variable> lv = fi.getArguments();
				for(Variable v: lv) {
					rtc.context.addVarToContext(v);
				}

				//Both return and the method have metadata
				String thisName = String.format(rtc.thisFormat, className);
				rtc.context.addInstanceToContext(thisName, c.getReference(), new Predicate());
				String returnVarName = String.format(retNameFormat,rtc.context.getCounter());
				Constraint cretRef = rtc.getRefinement(ret.getReturnedExpression())
										.substituteVariable(rtc.WILD_VAR, returnVarName)
										.substituteVariable(rtc.THIS, returnVarName);
				Constraint cexpectedType = fi.getRefReturn()
										.substituteVariable(rtc.WILD_VAR, returnVarName)
										.substituteVariable(rtc.THIS, returnVarName);

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
			System.out.println("function found:"+f);
			if(f != null) {//inside rtc.context
				checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(), 
						method.getSimpleName(), ctype);

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


	RefinedFunction getRefinementFunction(String methodName, String className, int size) {
		RefinedFunction f = rtc.context.getFunction(methodName, className, size);
		if(f == null)
			f = rtc.context.getFunction(String.format("%s.%s", className, methodName), methodName, size);
		return f;
	}

	


	private void searchMethodInLibrary(Method m, CtInvocation<?> invocation) {
		String ctype = m.getDeclaringClass().getCanonicalName();
		if(rtc.context.getFunction(m.getName(),ctype) != null) {//inside rtc.context
			checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(),
					m.getName(), ctype);
			return;
		}else {
			String name = m.getName();
			String prefix = m.getDeclaringClass().getCanonicalName();
			String completeName = String.format("%s.%s", prefix, name);
			if(rtc.context.getFunction(completeName, ctype) != null) {
				checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(),
						completeName, ctype);
			}

		}

	}

	private <R> void checkInvocationRefinements(CtElement element, List<CtExpression<?>> arguments, CtExpression<?> target,
			String methodName, String className) {
		int si = arguments.size();
		RefinedFunction f = rtc.context.getFunction(methodName, className, si);
		Map<String,String> map = mapInvocation(arguments, f);
		if(target!= null) {
			AuxStateHandler.checkTargetChanges(rtc, f, target, map, element);	
		}
		if(f.allRefinementsTrue()) {
			element.putMetadata(rtc.REFINE_KEY, new Predicate());
			return;
		}
		checkParameters(element, arguments, f, map);

		Constraint methodRef = f.getRefReturn(); 
		
		if(methodRef != null) {
			boolean equalsThis = methodRef.toString().equals("(_ == this)"); //TODO change for better
			List<String> vars = methodRef.getVariableNames(); 
			for(String s:vars) 
				if(map.containsKey(s))
					methodRef = methodRef.substituteVariable(s, map.get(s));

			String varName = null;
			if(element.getMetadata(rtc.TARGET_KEY) != null) {
				VariableInstance vi = (VariableInstance)element.getMetadata(rtc.TARGET_KEY);
				methodRef= methodRef.substituteVariable(rtc.THIS, vi.getName());
				Variable v = rtc.context.getVariableFromInstance(vi);
				if(v != null)
					varName = v.getName();
			}

			String viName = String.format(rtc.instanceFormat, f.getName(), rtc.context.getCounter());
			VariableInstance vi = (VariableInstance) rtc.context.addInstanceToContext(viName					, 
							f.getType(), methodRef.substituteVariable(rtc.WILD_VAR, viName));
			if(varName != null && f.hasStateChange() && equalsThis)
				rtc.context.addRefinementInstanceToVariable(varName, viName);
			element.putMetadata(rtc.TARGET_KEY, vi);
			element.putMetadata(rtc.REFINE_KEY, methodRef);
		}

	}


	private <R> Map<String, String> mapInvocation(List<CtExpression<?>> arguments, RefinedFunction f){
		Map<String, String> mapInvocation = new HashMap<>();
		List<CtExpression<?>> invocationParams = arguments;
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
		if(met == null)
			met = new Predicate();
		if(!met.getVariableNames().contains(rtc.WILD_VAR))
			met = new EqualsPredicate(new VariablePredicate(rtc.WILD_VAR), met);
		String nVar = String.format(rtc.instanceFormat, fArg.getName(),
				rtc.context.getCounter());
		rtc.context.addInstanceToContext(nVar, fArg.getType(), 
				met.substituteVariable(rtc.WILD_VAR, nVar));
		return nVar;
	}

	private <R> void checkParameters(CtElement invocation, List<CtExpression<?>> arguments, RefinedFunction f, Map<String, String> map) {	
		List<CtExpression<?>> invocationParams = arguments;
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

	void loadFunctionInfo(CtMethod<?> method) {
		String className = null;
		if(method.getParent() instanceof CtClass) {
			className = ((CtClass) method.getParent()).getQualifiedName();
		}else if(method.getParent() instanceof CtInterface<?>) {
			className = ((CtInterface<?>) method.getParent()).getQualifiedName();
		}
		if(className != null) {
			RefinedFunction fi = getRefinementFunction(method.getSimpleName(), className, method.getParameters().size());
			if( fi != null ) {
				List<Variable> lv = fi.getArguments();
				for(Variable v: lv)
					rtc.context.addVarToContext(v);
			}else {
				assertFalse("Method should already be in context. Should not arrive this point in refinement checker.", true);
				getMethodRefinements(method); //should be irrelevant -should never need to get here
			}
		}
		
	}




}
