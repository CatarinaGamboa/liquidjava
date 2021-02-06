package repair.regen.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import repair.regen.processor.built_ins.RefinementsLibrary;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
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

	}


	private CtTypeReference<?> getType(Class<?> type) {
		return rtc.factory.createCtTypeReference(type);
	}


	private <R> void checkInvocationRefinements(CtInvocation<R> invocation, String methodName) {
		RefinedFunction f = rtc.context.getFunctionByName(methodName);
		Constraint methodRef = f.getRenamedReturn();
		Constraint metRef = f.getRenamedReturn();

		Constraint metRefOriginal = f.getRefReturn(); 
		List<RefinedVariable> saveVars = new ArrayList<>();
		//for(RefinedVariable v: rtc.vcChecker.getVariables()) //TODO CHECK
			//saveVars.add(v);

		HashMap<String, String> newNames = new HashMap<>();

		if(methodRef != null) {
			//Checking Parameters
			List<CtExpression<?>> exps = invocation.getArguments();
			List<Variable> params = f.getArgRefinements();
			for (int i = 0; i < params.size(); i++) {
				RefinedVariable pinfo = params.get(i);
				CtExpression<?> exp = exps.get(i);
				String paramOriginalName = pinfo.getName();
				String newParamName = paramOriginalName+"_"+rtc.context.getCounter()+"_";
				newNames.put(paramOriginalName, newParamName);


				Constraint refPar = f.getRefinementsForParamIndex(i).substituteVariable(paramOriginalName, newParamName);
				Constraint refInv = rtc.getRefinement(exp).substituteVariable(rtc.WILD_VAR, newParamName);

				List<String> names = refPar.getVariableNames();
				for(String entry: newNames.keySet()) {
					if(!entry.equals(paramOriginalName) && names.contains(entry))
						refPar = refPar.substituteVariable(entry, newNames.get(entry));

				}

				RefinedVariable vi = rtc.context.addInstanceToContext(newParamName, pinfo.getType(), refInv);
				rtc.context.newRefinementToVariableInContext(newParamName, refInv);
				rtc.addRefinementVariable(vi);
				for(RefinedVariable s:saveVars)
					rtc.addRefinementVariable(s);

				metRefOriginal = metRefOriginal.substituteVariable(pinfo.getName(), newParamName);

				if(exp instanceof CtVariableRead<?>) {
					String name = ((CtVariableRead) exp).getVariable().getSimpleName();
					RefinedVariable rv = rtc.context.addVarToContext(name,
							((CtVariableRead) exp).getType(), refPar);
					rtc.addRefinementVariable(rv);
					metRef = metRef.substituteVariable(newParamName, name);
				}
				rtc.checkSMT(refPar, invocation);
				saveVars.add(vi);
				rtc.context.addRefinementInstanceToVariable(pinfo.getName(), vi.getName());
			}


			for(RefinedVariable s: saveVars)
				rtc.addRefinementVariable(s);

			invocation.putMetadata(rtc.REFINE_KEY, metRefOriginal);
		}
	}


	<R> void getMethodRefinements(CtMethod<R> method) {
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
		rtc.context.addFunctionToContext(f);
		for(CtAnnotation<? extends Annotation> ann :method.getAnnotations()) {
			if( !ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement"))
				continue;
			CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
			String methodRef = s.getValue();
			List<CtParameter<?>> params = method.getParameters();

			String ref = handleFunctionRefinements(f, methodRef, params);
			method.putMetadata(rtc.REFINE_KEY, new Predicate(ref));
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
		//TODO CHANGE
		String[] r = methodRef.split("}\\s*->\\s*\\{");

		StringBuilder sb = new StringBuilder();

		//For syntax {param1} -> {param2} -> ... -> {return}
		for (int i = 0; i < params.size(); i++) {
			CtParameter<?> param = params.get(i);
			String name = param.getSimpleName();
			String metRef = "("+r[i].replace("{", "").replace("}", "").replace(rtc.WILD_VAR, name)+")";
			Constraint cmetRef = new Predicate(metRef);
			param.putMetadata(rtc.REFINE_KEY, cmetRef);
			sb.append(sb.length() == 0? metRef : " && "+metRef);

			f.addArgRefinements(name,param.getType(), cmetRef);
			RefinedVariable rv = rtc.context.addVarToContext(name, param.getType(), cmetRef);
			rtc.addRefinementVariable(rv);
		}
		String retRef = "("+r[r.length-1].replace("{", "").replace("}", "")+")";
		f.setRefReturn(new Predicate(retRef));
		rtc.context.addFunctionToContext(f);
		String ss = sb.length()>0?" && "+ retRef:retRef;
		return sb.append(ss).toString();
	}

	private String handleFunctionRefinements(String methodRef, Method method) {
		Parameter[] params = method.getParameters();
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getName());
		f.setType(getType(method.getReturnType()));
		f.setRefReturn(new Predicate());

		String[] r = methodRef.split("}\\s*->\\s*\\{");

		StringBuilder sb = new StringBuilder();

		//For syntax {param1} -> {param2} -> ... -> {return}
		for (int i = 0; i < params.length; i++) {
			Parameter param = params[i];
			String name = param.getName();
			String metRef = "("+r[i].replace("{", "").replace("}", "").replace(rtc.WILD_VAR, name)+")";
			//param.putMetadata(rtc.REFINE_KEY, metRef);
			sb.append(sb.length() == 0? metRef : " && "+metRef);

			Constraint cmetRef = new Predicate(metRef);
			f.addArgRefinements(name,getType(param.getType()), cmetRef);
			RefinedVariable rv = rtc.context.addVarToContext(name, getType(param.getType()), cmetRef);
			rtc.addRefinementVariable(rv);
		}
		String retRef = "("+r[r.length-1].replace("{", "").replace("}", "")+")";
		f.setRefReturn(new Predicate(retRef));
		rtc.context.addFunctionToContext(f);
		String ss = sb.length()>0?" && "+ retRef:retRef;
		return sb.append(ss).toString();
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
			for(RefinedVariable vi:fi.getArgRefinements())
				rtc.addRefinementVariable(vi);

			//Both return and the method have metadata
			String returnVarName = "RET_"+rtc.context.getCounter();
			Constraint cretRef = rtc.getRefinement(ret.getReturnedExpression()).substituteVariable(rtc.WILD_VAR, returnVarName);
			Constraint cexpectedType = fi.getRefReturn().substituteVariable(rtc.WILD_VAR, returnVarName);

			RefinedVariable rv = rtc.context.addVarToContext(returnVarName, method.getType(), cretRef);
			rtc.addRefinementVariable(rv);
			rtc.checkSMT(cexpectedType, ret);
			rtc.context.newRefinementToVariableInContext(returnVarName, cexpectedType);
		}
	}

}
