package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import repair.regen.processor.built_ins.RefinementsLibrary;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;

import java.util.Optional;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.declaration.CtMethodImpl;

public class MethodsFunctionsChecker {

	private RefinementTypeChecker rtc;
	private RefinementsLibrary lib;

	public MethodsFunctionsChecker(RefinementTypeChecker rtc) {
		this.rtc = rtc; 
		lib = rtc.lib;
		
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
			String name = m.toGenericString();
			int lastSpaceI = name.lastIndexOf(" ");
			name = name.substring(lastSpaceI+1);
			Optional<String> ref = lib.getRefinement(name);
			if(ref.isPresent()) {
				String metRef = handleFunctionRefinements(ref.get(), m);//maybe here error?
				System.out.println(metRef);
				checkInvocationRefinements(invocation, m.getName());
			}
//			handleFunctionRefinements(FunctionInfo f, String methodRef, List<CtParameter<?>> params)
		
	}


	private CtTypeReference<?> getType(Class<?> type) {
		return rtc.factory.createCtTypeReference(type);
	}


	private <R> void checkInvocationRefinements(CtInvocation<R> invocation, String methodName) {
		RefinedFunction f = rtc.context.getFunctionByName(methodName);
		String methodRef = f.getRenamedReturn();
		String metRef = f.getRenamedReturn();
		
		String metRefOriginal = f.getRefReturn(); 
		List<String> saveVars = new ArrayList<>();
		for(String v: rtc.vcChecker.getVariables())
			saveVars.add(v);
		
		HashMap<String, String> newNames = new HashMap<>();

		if(methodRef != null) {
			//Checking Parameters
			List<CtExpression<?>> exps = invocation.getArguments();
			List<RefinedVariable> params = f.getArgRefinements();
			for (int i = 0; i < params.size(); i++) {
				RefinedVariable pinfo = params.get(i);
				CtExpression<?> exp = exps.get(i);
				String paramOriginalName = pinfo.getName();
				String newParamName = paramOriginalName+"_"+rtc.context.getCounter()+"_";
				newNames.put(paramOriginalName, newParamName);
				
				String refPar = f.getRefinementsForParamIndex(i).replace(paramOriginalName, newParamName);
				String refInv = (rtc.getRefinement(exp)).replace(rtc.WILD_VAR, newParamName);
				
				for(String entry: newNames.keySet()) {
					if(!entry.equals(paramOriginalName) && refPar.contains(entry))
						refPar = refPar.replace(entry, newNames.get(entry));
				}

				RefinedVariable vi = rtc.context.addVarToContext(newParamName, pinfo.getType(), refInv);
				rtc.context.newRefinementToVariableInContext(newParamName, refInv);
				rtc.addRefinementVariable(newParamName);
				for(String s:saveVars)
					rtc.addRefinementVariable(s);
				
				metRefOriginal = metRefOriginal.replace(pinfo.getName(), newParamName);

				if(exp instanceof CtVariableRead<?>) {
					String name = ((CtVariableRead) exp).getVariable().getSimpleName();
					rtc.context.addVarToContext(name,
							((CtVariableRead) exp).getType(), refPar);
					rtc.addRefinementVariable(name);
					metRef = metRef.replace(newParamName, name);
				}
				rtc.checkSMT(refInv, refPar, invocation);
				saveVars.add(newParamName);
				rtc.context.addRefinementInstanceToVariable(pinfo.getName(), vi.getName());
			}


			for(String s: saveVars)
				rtc.addRefinementVariable(s);
			
			//Checking Return
			String s = methodRef;
			//String s = methodRef;
			invocation.putMetadata(rtc.REFINE_KEY, metRefOriginal);
		}
	}


	<R> void getMethodRefinements(CtMethod<R> method) {
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn("true");
		rtc.context.addFunctionToContext(f);
		for(CtAnnotation<? extends Annotation> ann :method.getAnnotations()) {
			if( !ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement"))
				continue;
			CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
			String methodRef = s.getValue();
			List<CtParameter<?>> params = method.getParameters();
			
			String ref = handleFunctionRefinements(f, methodRef, params);
			method.putMetadata(rtc.REFINE_KEY, ref);
		}
	}


	/**
	 * Adds information to FunctionInfo according to the refinements 
	 * @param f
	 * @param methodRef
	 * @param params
	 * @return
	 */
	private String handleFunctionRefinements(RefinedFunction f, String methodRef, List<CtParameter<?>> params) {
		String[] r = methodRef.split("}\\s*->\\s*\\{");
		
		StringBuilder sb = new StringBuilder();

		//For syntax {param1} -> {param2} -> ... -> {return}
		for (int i = 0; i < params.size(); i++) {
			CtParameter<?> param = params.get(i);
			String name = param.getSimpleName();
			String metRef = "("+r[i].replace("{", "").replace("}", "").replace(rtc.WILD_VAR, name)+")";
			param.putMetadata(rtc.REFINE_KEY, metRef);
			sb.append(sb.length() == 0? metRef : " && "+metRef);

			f.addArgRefinements(name,param.getType(), metRef);
			rtc.context.addVarToContext(name, param.getType(), metRef);
			rtc.addRefinementVariable(name);
		}
		String retRef = "("+r[r.length-1].replace("{", "").replace("}", "")+")";
		f.setRefReturn(retRef);
		rtc.context.addFunctionToContext(f);
		
		return sb.append(" && "+ retRef).toString();
	}
	
	private String handleFunctionRefinements(String methodRef, Method method) {
		Parameter[] params = method.getParameters();
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getName());
		f.setType(getType(method.getReturnType()));
		f.setRefReturn("true");
		
		String[] r = methodRef.split("}\\s*->\\s*\\{");
		
		StringBuilder sb = new StringBuilder();
		
		//For syntax {param1} -> {param2} -> ... -> {return}
		for (int i = 0; i < params.length; i++) {
			Parameter param = params[i];
			String name = param.getName();
			String metRef = "("+r[i].replace("{", "").replace("}", "").replace(rtc.WILD_VAR, name)+")";
			//param.putMetadata(rtc.REFINE_KEY, metRef);
			sb.append(sb.length() == 0? metRef : " && "+metRef);

			f.addArgRefinements(name,getType(param.getType()), metRef);
			rtc.context.addVarToContext(name, getType(param.getType()), metRef);
			rtc.addRefinementVariable(name);
		}
		String retRef = "("+r[r.length-1].replace("{", "").replace("}", "")+")";
		f.setRefReturn(retRef);
		rtc.context.addFunctionToContext(f);
		return sb.append(" && "+ retRef).toString();
	}



	<R> void getReturnRefinements(CtReturn<R> ret) {
		if(ret.getReturnedExpression() != null) {
			//check if there are refinements
			if(rtc.getRefinement(ret.getReturnedExpression())== null)
				ret.getReturnedExpression().putMetadata(rtc.REFINE_KEY, "true");
			CtMethod method = ret.getParent(CtMethod.class);
			//check if method has refinements
			if(rtc.getRefinement(method) == null)
				return;
			RefinedFunction fi = rtc.context.getFunctionByName(method.getSimpleName());
			for(RefinedVariable vi:fi.getArgRefinements())
				rtc.addRefinementVariable(vi.getName());

			//Both return and the method have metadata
			String returnVarName = "RET_"+rtc.context.getCounter(); 
			String retRef = String.format("(%s)", rtc.getRefinement(ret.getReturnedExpression())
					.replace(rtc.WILD_VAR, returnVarName));
			String expectedType = fi.getRefReturn().replace(rtc.WILD_VAR, returnVarName);

			rtc.context.addVarToContext(returnVarName, method.getType(), retRef);
			rtc.addRefinementVariable(returnVarName);
			rtc.checkSMT(retRef, expectedType, ret);
			rtc.context.newRefinementToVariableInContext(returnVarName, expectedType);
		}
	}

}
