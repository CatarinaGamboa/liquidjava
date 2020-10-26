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
	
	
	public Context() {
		ctxVars.add(new ArrayList<>());
	}
	
	private Map<String, CtTypeReference<?>> getContext() {
		Map<String, CtTypeReference<?>> ret = new HashMap<>();
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				ret.put(var.getName(), var.getType());
			}
		}
		return ret;
	}
}
