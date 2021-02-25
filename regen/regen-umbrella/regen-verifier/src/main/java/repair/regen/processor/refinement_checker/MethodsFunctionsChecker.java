package repair.regen.processor.refinement_checker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.EqualsPredicate;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import repair.regen.processor.context.VariableInstance;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class MethodsFunctionsChecker {

	private TypeChecker rtc;
	
	private static String retNameFormat = "#ret_%d";

	public MethodsFunctionsChecker(TypeChecker rtc) {
		this.rtc = rtc; 

	}

	<R> void getInvocationRefinements(CtInvocation<R> invocation) {
		CtExecutable<?> method = invocation.getExecutable().getDeclaration();
		if(method == null) {
			Method m = invocation.getExecutable().getActualMethod();
			if(m != null) searchMethodInLibrary(m, invocation);
		}else {
			 if(rtc.context.getFunctionByName(method.getSimpleName()) != null) {//inside rtc.context
				checkInvocationRefinements(invocation, method.getSimpleName());	
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





	private void searchMethodInLibrary(Method m, CtInvocation<?> invocation) {
		if(rtc.context.getFunctionByName(m.getName()) != null) {//inside rtc.context
			checkInvocationRefinements(invocation, m.getName());
			return;
		}else {
			String name = m.getName();
			String prefix = m.getDeclaringClass().getCanonicalName();
			String completeName = String.format("%s.%s", prefix, name);
			if(rtc.context.getFunctionByName(completeName) != null) {
				checkInvocationRefinements(invocation, completeName);
			}

		}

	}

	private <R> void checkInvocationRefinements(CtInvocation<R> invocation, String methodName) {
//		invocation.getTarget().getType().toString()
		RefinedFunction f;
		if(invocation.getTarget() != null)
			f = rtc.context.getFunction(methodName, invocation.getTarget().getType().toString());
		else
			f = rtc.context.getFunctionByName(methodName);
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
			if(iArg instanceof CtLiteral)
				invStr = iArg.toString();
			else if(iArg instanceof CtFieldRead) {
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
			met = new EqualsPredicate(rtc.WILD_VAR, met);
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

	<R> void getMethodRefinements(CtMethod<R> method) {
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
		if(method.getParent() instanceof CtClass) {
			CtClass klass = (CtClass)method.getParent();
			f.setClass(klass.getQualifiedName());
		}
		rtc.context.addFunctionToContext(f);
		auxGetMethodRefinements(method, f);
		
	}

	<R> void getMethodRefinements(CtMethod<R> method, String prefix) {
		RefinedFunction f = new RefinedFunction();
		f.setName(String.format("%s.%s", prefix, method.getSimpleName()));
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
		rtc.context.addGlobalFunctionToContext(f);
		auxGetMethodRefinements(method, f);
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
			Constraint c = oc.isPresent()?oc.get():new Predicate();
			c = c.substituteVariable(rtc.WILD_VAR, paramName);
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
	



	<R> void getReturnRefinements(CtReturn<R> ret) {
		if(ret.getReturnedExpression() != null) {
			//check if there are refinements
			if(rtc.getRefinement(ret.getReturnedExpression())== null)
				ret.getReturnedExpression().putMetadata(rtc.REFINE_KEY, new Predicate());
			CtMethod method = ret.getParent(CtMethod.class);
			//check if method has refinements
			if(rtc.getRefinement(method) == null)
				return;
			RefinedFunction fi = rtc.context.getFunctionByName(method.getSimpleName());
			
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
