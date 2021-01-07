package repair.regen.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;

public class MethodsFunctionsChecker {

	private Context context;
	private RefinementTypeChecker rtc;

	public MethodsFunctionsChecker(RefinementTypeChecker rtc) {
		this.rtc = rtc; 
		context = rtc.context;
	}
	<R> void getInvocationRefinements(CtInvocation<R> invocation) {
		CtExecutable<?> method = invocation.getExecutable().getDeclaration();
		if(method != null) {
			FunctionInfo f = context.getFunctionByName(method.getSimpleName());
			String methodRef = f.getRenamedReturn();
			String metRef = f.getRenamedReturn();
			List<String> saveVars = new ArrayList<>();
			for(String v: rtc.vcChecker.getVariables())
				saveVars.add(v);

			if(methodRef != null) {
				//Checking Parameters
				List<CtExpression<?>> exps = invocation.getArguments();
				List<VariableInfo> params = f.getArgRefinements();
				for (int i = 0; i < params.size(); i++) {
					VariableInfo pinfo = params.get(i);
					CtExpression<?> exp = exps.get(i);
					String newParamName = pinfo.getIncognitoName();
					String refPar = f.getRefinementsForParamIndex(i);
					String refInv = (rtc.getRefinement(exp)).replace(rtc.WILD_VAR, newParamName);
					System.out.println("ref par:"+refPar);

					context.addVarToContext(newParamName, pinfo.getType(), refInv);
					context.newRefinementToVariableInContext(newParamName, refInv);
					rtc.addRefinementVariable(newParamName);
					for(String s:saveVars)
						rtc.addRefinementVariable(s);

					if(exp instanceof CtVariableRead<?>) {
						String name = ((CtVariableRead) exp).getVariable().getSimpleName();
						context.addVarToContext(name,
								((CtVariableRead) exp).getType(), refPar);
						rtc.addRefinementVariable(name);
						metRef = metRef.replaceAll(newParamName, name);
					}
					rtc.checkSMT(refInv, refPar, (CtVariable<?>)method.getParameters().get(i));
					saveVars.add(newParamName);
				}


				for(VariableInfo vi:params) 
					rtc.addRefinementVariable(vi.getIncognitoName());
				for(String s: saveVars)
					rtc.addRefinementVariable(s);
				//Checking Return
				String s = methodRef;// sb.length() == 0? methodRef:  sb.append(" && "+methodRef).toString();
				//String s = methodRef;
				invocation.putMetadata(rtc.REFINE_KEY, metRef);
			}
		}
	}
	
	<R> void getMethodRefinements(CtMethod<R> method) {
		FunctionInfo f = new FunctionInfo();
		f.setName(method.getSimpleName());
		f.setType(method.getType());
		f.setRefReturn("true");
		context.addFunctionToContext(f);
		for(CtAnnotation<? extends Annotation> ann :method.getAnnotations()) {
			if( !ann.getActualAnnotation().annotationType().getCanonicalName()
					.contentEquals("repair.regen.specification.Refinement"))
				continue;
			CtLiteral<String> s = (CtLiteral<String>) ann.getAllValues().get("value");
			String methodRef = s.getValue();
			List<CtParameter<?>> params = method.getParameters();
			String[] r = methodRef.split("->");
			StringBuilder sb = new StringBuilder();

			//For syntax {param1} -> {param2} -> ... -> {return}
			for (int i = 0; i < params.size(); i++) {
				CtParameter<?> param = params.get(i);
				String name = param.getSimpleName();
				String metRef = r[i].replace("{", "(").replace("}", ")").replace(rtc.WILD_VAR, name);
				param.putMetadata(rtc.REFINE_KEY, metRef);
				sb.append(sb.length() == 0? metRef : " && "+metRef);

				f.addArgRefinements(name,param.getType(), metRef);
				context.addVarToContext(name, param.getType(), metRef);
				rtc.addRefinementVariable(name);
			}
			String retRef = r[r.length-1].replace("{", "(").replace("}", ")");
			f.setRefReturn(retRef);

			method.putMetadata(rtc.REFINE_KEY, sb.append(" && "+ retRef).toString());

		}

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
			FunctionInfo fi = context.getFunctionByName(method.getSimpleName());
			for(VariableInfo vi:fi.getArgRefinements())
				rtc.addRefinementVariable(vi.getName());

			//Both return and the method have metadata
			String returnVarName = "RET_"+context.getCounter(); 
			String retRef = String.format("(%s)", rtc.getRefinement(ret.getReturnedExpression())
													.replace(rtc.WILD_VAR, returnVarName));
			String expectedType = fi.getRefReturn().replace(rtc.WILD_VAR, returnVarName);

			context.addVarToContext(returnVarName, method.getType(), retRef);
			rtc.addRefinementVariable(returnVarName);
			rtc.checkSMT(retRef, expectedType, ret);
			context.removeRefinementFromVariableInContext(returnVarName, retRef);
			context.newRefinementToVariableInContext(returnVarName, expectedType);
		}
	}

}
