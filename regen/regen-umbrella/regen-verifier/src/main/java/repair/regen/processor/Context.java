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
	private List<VariableInfo> ctxSpecificVars;
	
	public int counter;
	private static Context instance;


	private Context() {
		ctxVars = new Stack<>();
		ctxVars.add(new ArrayList<>());//global vars
		ctxFunctions = new ArrayList<>();
		ctxSpecificVars = new ArrayList<>();
		counter = 0;

	}

	//SINGLETON
	public static Context getInstance() {
		if(instance == null)
			instance = new Context() ;
		return instance;
	}

	public void reinitializeContext() {
		ctxVars = new Stack<>();
		ctxVars.add(new ArrayList<>());//global vars
		ctxFunctions = new ArrayList<>();
		ctxSpecificVars = new ArrayList<>();
		counter = 0;
	}

	public void enterContext() {
		ctxVars.push(new ArrayList<>());
		//make each variable enter context
		for(VariableInfo vi: getAllVariables())
			vi.enterContext();

	}

	public void exitContext() {
		ctxVars.pop();
		//make each variable exit context
		for(VariableInfo vi: getAllVariables())
			vi.exitContext();
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
		for(VariableInfo var: ctxSpecificVars)
			ret.put(var.getName(), var.getType());
		return ret;
	}

	public void addVarToContext(VariableInfo var) {
		if(!hasVariable(var.getName()))
			ctxVars.peek().add(var);
	}

	public VariableInfo addVarToContext(String name, CtTypeReference<?> type, String refinements) {
		VariableInfo vi =new VariableInfo(name, type, refinements);
		addVarToContext(vi);
		return vi;
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
	
	
	public void variablesSetBeforeIf() {
		for(VariableInfo vi: getAllVariables())
			vi.saveInstanceBeforeIf();
	}
	public void variablesSetThenIf() {
		for(VariableInfo vi: getAllVariables())
			vi.saveInstanceThen();
	}
	public void variablesSetElseIf() {
		for(VariableInfo vi: getAllVariables())
			vi.saveInstanceElse();
	}
	public void variablesCombineFromIf() {
		for(VariableInfo vi: getAllVariables()) {
			Optional<VariableInfo>ovi = vi.getIfInstanceCombination(getCounter());
			if(ovi.isPresent()) {
				VariableInfo vii = ovi.get();
				addVarToContext(vii);
				addRefinementInstanceToVariable(vi.getName(), vii.getName());
				
			}
		}
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
		for(VariableInfo var: ctxSpecificVars) {
			if(var.getName().equals(name))
				return var;
		}
		return null;
	}

	public boolean hasVariable(String name) {
		return getVariableByName(name)!= null;
	}

	public String allVariablesToString() {
		StringBuilder sb = new StringBuilder();
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				sb.append(var.toString()+"; ");
			}
		}
		return sb.toString();
	}

	/**
	 * Lists all variables inside the stack
	 * @return
	 */
	public List<VariableInfo> getAllVariables() {
		List<VariableInfo> lvi = new ArrayList<>();
		for(List<VariableInfo> l: ctxVars) {
			for(VariableInfo var: l) {
				lvi.add(var);
			}
		}
		return lvi;
	}

	public void addRefinementInstanceToVariable(String name, String name2) {
		if(!hasVariable(name) || !hasVariable(name2)) return;

		VariableInfo vi1 = getVariableByName(name);
		VariableInfo vi2 = getVariableByName(name2);
		vi1.addInstance(getVariableByName(name2));
		addSpecificVariable(vi2);
	}

	public Optional<VariableInfo> getLastVariableInstance(String name) {
		if(!hasVariable(name)) return Optional.empty();
		return getVariableByName(name).getLastInstance();
	}
	
	void addSpecificVariable(VariableInfo vi) {
		ctxSpecificVars.add(vi);
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
