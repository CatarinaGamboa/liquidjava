package repair.regen.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import spoon.reflect.reference.CtTypeReference;

public class Context {
	private Stack<List<VariableInfo>> ctxVars = new Stack<>();
	private List<FunctionInfo> ctxFunctions = new ArrayList<>();
	public static int counter = 0;
	
	
	public Context() {
		ctxVars.add(new ArrayList<>());
	}
	
	public Map<String, CtTypeReference<?>> getContext() {
		Map<String, CtTypeReference<?>> ret = new HashMap<>();
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				ret.put(var.getName(), var.getType());
			}
		}
		return ret;
	}
	
	public void addVarToContext(VariableInfo var) {
		if( !ctxVars.peek().contains(var))
			ctxVars.peek().add(var);
	}
	public void addVarToContext(String name, CtTypeReference<?> type, String refinements) {
		addVarToContext(new VariableInfo(name, type, refinements));
	}
	
	public void addFunctionToContext(FunctionInfo f) {
		ctxFunctions.add(f);
	}
	
	public void enterContext() {
		ctxVars.add(new ArrayList<>());
	}
	
	public void exitContext() {
		ctxVars.pop();
	}
	
	public String getAllVariables() {
		StringBuilder sb = new StringBuilder();
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				sb.append(var.toString()+"; ");
			}
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		System.out.println("Variables");
		for(List<VariableInfo> l : ctxVars) {
			sb.append("{");
			for(VariableInfo var: l) {
				sb.append(var.toString()+"; ");
			}
			sb.append("}\n");
		}
		return sb.toString();
	}
	
}
