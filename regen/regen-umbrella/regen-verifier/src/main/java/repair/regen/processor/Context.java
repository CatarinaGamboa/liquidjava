package repair.regen.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;

public class Context {
	private Stack<List<VariableInfo>> ctxVars;
	private List<FunctionInfo> ctxFunctions;
	
	public int counter;
	private static Context instance;
	
	
	private Context() {
		ctxVars = new Stack<>();
		ctxVars.add(new ArrayList<>());//global vars
		ctxFunctions = new ArrayList<>();
		counter = 0;
		
	}
	
	//SINGLETON
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
	}
	
	public void exitContext() {
		ctxVars.pop();
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
	
	public void addRefinementToVariableInContext(CtVariable<?> variable, String et) {
		String name = variable.getSimpleName();
		if(hasVariable(name)){
			VariableInfo vi = getVariableByName(name);
			String oldRef = vi.getRefinement();
			vi.newRefinement(et);
		}else {
			addVarToContext(name, variable.getType(), et);
		}
	}
	
	
	public void newRefinementToVariableInContext(CtVariable<?> variable, String expectedType) {
		String name = variable.getSimpleName();
		if(hasVariable(name)){
			VariableInfo vi = getVariableByName(name);
			vi.newRefinement("("+expectedType+")");
		}else
			addVarToContext(name, variable.getType(), expectedType);
	}
	
	/**
	 * The variable with name variableName will have a new refinement
	 * @param variableName
	 * @param expectedType
	 */
	public void newRefinementToVariableInContext(String variableName, String expectedType) {
		if(hasVariable(variableName)){
			VariableInfo vi = getVariableByName(variableName);
			vi.newRefinement("("+expectedType+")");
		}
	}
	
	
	public String getVariableRefinements(String varName) {
		return hasVariable(varName)?getVariableByName(varName).getRefinement() : ""; 
	}
	
	public void removeRefinementFromVariableInContext(CtVariable<?> variable, String et) {
		VariableInfo vi = getVariableByName(variable.getSimpleName());
		vi.removeRefinement(et);
	}
	public void removeRefinementFromVariableInContext(String variableName, String et) {
		VariableInfo vi = getVariableByName(variableName);
		vi.removeRefinement(et);
	}
	
	public void addMainRefinementVariable(String name, String refinement) {
		VariableInfo vi = getVariableByName(name);
		vi.setMainRefinement(refinement);
	}

	public String getMainRefinementVariable(String name) {
		VariableInfo vi = getVariableByName(name);
		return vi.getMainRefinement();
	}

	
	
	public void addFunctionToContext(FunctionInfo f) {
		if(!ctxFunctions.contains(f))
			ctxFunctions.add(f);
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
	
	public void addRefinementInstanceToVariable(String name, String name2) {
		if(!hasVariable(name) || !hasVariable(name2)) return;
		
		VariableInfo vi1 = getVariableByName(name);
		vi1.addInstance(getVariableByName(name2));
	}
	
	public Optional<VariableInfo> getLastVariableInstance(String name) {
		if(!hasVariable(name)) return Optional.empty();
		return getVariableByName(name).getLastInstance();
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
