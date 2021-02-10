package repair.regen.processor.refinement_checker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.RefinedFunction;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.processor.context.Variable;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

public class MethodsFunctionsChecker {

	private TypeChecker rtc;

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
		RefinedFunction f = rtc.context.getFunctionByName(methodName);
		Constraint methodRef = f.getRenamedReturn();
		Constraint metRef = f.getRenamedReturn();

		Constraint metRefOriginal = f.getRefReturn(); 
		List<RefinedVariable> saveVars = new ArrayList<>();


		HashMap<String, String> newNames = new HashMap<>();

		if(methodRef != null) {
			//Checking Parameters
			List<CtExpression<?>> exps = invocation.getArguments();
			List<Variable> params = f.getArguments();
			for (int i = 0; i < params.size(); i++) {
				RefinedVariable pinfo = params.get(i);
				CtExpression<?> exp = exps.get(i);
				String paramOriginalName = pinfo.getName();
				String newParamName = String.format(rtc.instanceFormat, paramOriginalName, rtc.context.getCounter());
				newNames.put(paramOriginalName, newParamName);

				Constraint rP = f.getRefinementsForParamIndex(i);
				Constraint refPar = f.getRefinementsForParamIndex(i).substituteVariable(paramOriginalName, newParamName);
				Constraint refInv = rtc.getRefinement(exp).substituteVariable(rtc.WILD_VAR, newParamName);

				List<String> names = refPar.getVariableNames();
				for(String entry: newNames.keySet()) {
					if(!entry.equals(paramOriginalName) && names.contains(entry))
						refPar = refPar.substituteVariable(entry, newNames.get(entry));

				}

				RefinedVariable vi = rtc.context.addInstanceToContext(newParamName, pinfo.getType(), refInv);
				rtc.context.newRefinementToVariableInContext(newParamName, refInv);

				metRefOriginal = metRefOriginal.substituteVariable(pinfo.getName(), newParamName);

				if(exp instanceof CtVariableRead<?>) {
					String name = ((CtVariableRead) exp).getVariable().getSimpleName();
					RefinedVariable rv = rtc.context.addVarToContext(name,
							((CtVariableRead) exp).getType(), refPar);
					metRef = metRef.substituteVariable(newParamName, name);
				}
				rtc.checkSMT(refPar, invocation);
				saveVars.add(vi);
				rtc.context.addRefinementInstanceToVariable(pinfo.getName(), vi.getName());
			}

			invocation.putMetadata(rtc.REFINE_KEY, metRefOriginal);
		}
	}

	
	<R> void getMethodRefinements(CtMethod<R> method) {
		RefinedFunction f = new RefinedFunction();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn(new Predicate());
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
			String returnVarName = "RET_"+rtc.context.getCounter();
			Constraint cretRef = rtc.getRefinement(ret.getReturnedExpression()).substituteVariable(rtc.WILD_VAR, returnVarName);
			Constraint cexpectedType = fi.getRefReturn().substituteVariable(rtc.WILD_VAR, returnVarName);

			RefinedVariable rv = rtc.context.addVarToContext(returnVarName, method.getType(), cretRef);
			rtc.checkSMT(cexpectedType, ret);
			rtc.context.newRefinementToVariableInContext(returnVarName, cexpectedType);
		}
	}

}
