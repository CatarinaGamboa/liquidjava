package repair.regen.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import spoon.reflect.reference.CtTypeReference;

public class Context {
	private Stack<List<VariableInfo>> ctxVars;
	private List<FunctionInfo> ctxFunctions;
	private Stack<List<String>> ctxPathRefinements;
	
	public int counter;
	private static Context instance;
	
	
	private Context() {
		ctxVars = new Stack<>();
		ctxVars.add(new ArrayList<>());//global vars
		ctxPathRefinements = new Stack<>();
		ctxFunctions = new ArrayList<>();
		counter = 0;
		
	}
	
	public static Context getInstance() {
		if(instance == null)
			instance = new Context() ;
		return instance;
	}
	
	public void reinitializeContext() {
		instance = new Context();
	}
	
	public void enterContext() {
		ctxVars.add(new ArrayList<>());
		ctxPathRefinements.add(new ArrayList<>());
	}
	
	public void exitContext() {
		ctxVars.pop();
		ctxPathRefinements.pop();
	}
	
	public int getCounter() {
		return counter++;
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
		if(!hasVariable(var.getName()))
			ctxVars.peek().add(var);
	}
	
	public void addVarToContext(String name, CtTypeReference<?> type, String refinements) {
		addVarToContext(new VariableInfo(name, type, refinements));
	}
	
	public void addFunctionToContext(FunctionInfo f) {
		ctxFunctions.add(f);
	}
	
	public void addToPath(String ref) {
		if(!ctxPathRefinements.peek().contains(ref))
			ctxPathRefinements.peek().add(ref);
	}
	
	public FunctionInfo getFunctionByName(String name) {
		for(FunctionInfo fi: ctxFunctions) {
			if(fi.getName().equals(name))
				return fi;
		}
		return null;
	}
	
	public VariableInfo getVariableByName(String name) {
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				if(var.getName().equals(name))
					return var;
			}
		}
		return null;
	}
	
	public boolean hasVariable(String name) {
		return getVariableByName(name)!= null;
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
	
	public String getPathRefinements() {
		StringBuilder sb = new StringBuilder();
		for(List<String> l: ctxPathRefinements) {
			for(String ref: l) {
				sb.append(sb.length()==0? ref : " && "+ref);
			}
		}
		return sb.toString();
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("###########Variables############");
		for(List<VariableInfo> l : ctxVars) {
			sb.append("{");
			for(VariableInfo var: l) {
				sb.append(var.toString()+"; ");
			}
			sb.append("}\n");
		}
		sb.append("\n############Functions:############\n");
		for(FunctionInfo f : ctxFunctions)
			sb.append(f.toString());
		return sb.toString();
	}
	
}