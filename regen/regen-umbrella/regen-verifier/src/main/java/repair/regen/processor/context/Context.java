package repair.regen.processor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import repair.regen.processor.constraints.Constraint;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;

public class Context {
	private Stack<List<RefinedVariable>> ctxVars;
	private List<RefinedFunction> ctxFunctions;
	private List<RefinedVariable> ctxSpecificVars;
	
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
		for(RefinedVariable vi: getAllVariables())
			vi.enterContext();

	}

	public void exitContext() {
		ctxVars.pop();
		//make each variable exit context
		for(RefinedVariable vi: getAllVariables())
			vi.exitContext();
	}


	public int getCounter() {
		return counter++;
	}

	public Map<String, CtTypeReference<?>> getContext() {
		Map<String, CtTypeReference<?>> ret = new HashMap<>();
		for(List<RefinedVariable> l: ctxVars) {
			for(RefinedVariable var: l) {
				ret.put(var.getName(), var.getType());
			}
		}
		for(RefinedVariable var: ctxSpecificVars)
			ret.put(var.getName(), var.getType());
		return ret;
	}

	public void addVarToContext(RefinedVariable var) {
		if(!hasVariable(var.getName()))
			ctxVars.peek().add(var);
	}
	
	public RefinedVariable addVarToContext(String simpleName, CtTypeReference<?> type, Constraint c) {
		RefinedVariable vi =new RefinedVariable(simpleName, type, c);
		addVarToContext(vi);
		return vi;
		
	}
	public void addRefinementToVariableInContext(CtVariable<?> variable, Constraint et) {
		String name = variable.getSimpleName();
		if(hasVariable(name)){
			RefinedVariable vi = getVariableByName(name);
			vi.newRefinement(et);
		}else {
			addVarToContext(name, variable.getType(), et);
		}
	}

//TODO ERASE
	public void newRefinementToVariableInContext(CtVariable<?> variable, Constraint expectedType) {
		String name = variable.getSimpleName();
		if(hasVariable(name)){
			RefinedVariable vi = getVariableByName(name);
			vi.newRefinement(expectedType);
		}else
			addVarToContext(name, variable.getType(), expectedType);
	}

	/**
	 * The variable with name variableName will have a new refinement
	 * @param variableName
	 * @param expectedType
	 */
	public void newRefinementToVariableInContext(String variableName, Constraint expectedType) {
		if(hasVariable(variableName)){
			RefinedVariable vi = getVariableByName(variableName);
			vi.newRefinement(expectedType);
		}
	}


	public Constraint getVariableRefinements(String varName) {
		return hasVariable(varName)?getVariableByName(varName).getRefinement() : null; 
	}
	
	public void variablesSetBeforeIf() {
		for(RefinedVariable vi: getAllVariables())
			vi.saveInstanceBeforeIf();
	}
	public void variablesSetThenIf() {
		for(RefinedVariable vi: getAllVariables())
			vi.saveInstanceThen();
	}
	public void variablesSetElseIf() {
		for(RefinedVariable vi: getAllVariables())
			vi.saveInstanceElse();
	}
	public void variablesCombineFromIf() {
		for(RefinedVariable vi: getAllVariables()) {
			Optional<RefinedVariable>ovi = vi.getIfInstanceCombination(getCounter());
			if(ovi.isPresent()) {
				RefinedVariable vii = ovi.get();
				addVarToContext(vii);
				addRefinementInstanceToVariable(vi.getName(), vii.getName());
				
			}
		}
	}



	public void addFunctionToContext(RefinedFunction f) {
		if(!ctxFunctions.contains(f))
			ctxFunctions.add(f);
	}

	public RefinedFunction getFunctionByName(String name) {
		for(RefinedFunction fi: ctxFunctions) {
			if(fi.getName().equals(name))
				return fi;
		}
		return null;
	}

	public RefinedVariable getVariableByName(String name) {
		for(List<RefinedVariable> l: ctxVars) {
			for(RefinedVariable var: l) {
				if(var.getName().equals(name))
					return var;
			}
		}
		for(RefinedVariable var: ctxSpecificVars) {
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
		for(List<RefinedVariable> l: ctxVars) {
			for(RefinedVariable var: l) {
				sb.append(var.toString()+"; ");
			}
		}
		return sb.toString();
	}

	/**
	 * Lists all variables inside the stack
	 * @return
	 */
	public List<RefinedVariable> getAllVariables() {
		List<RefinedVariable> lvi = new ArrayList<>();
		for(List<RefinedVariable> l: ctxVars) {
			for(RefinedVariable var: l) {
				lvi.add(var);
			}
		}
		return lvi;
	}

	public void addRefinementInstanceToVariable(String name, String name2) {
		if(!hasVariable(name) || !hasVariable(name2)) return;

		RefinedVariable vi1 = getVariableByName(name);
		RefinedVariable vi2 = getVariableByName(name2);
		vi1.addInstance(getVariableByName(name2));
		addSpecificVariable(vi2);
	}

	public Optional<RefinedVariable> getLastVariableInstance(String name) {
		if(!hasVariable(name)) return Optional.empty();
		return getVariableByName(name).getLastInstance();
	}
	
	public void addSpecificVariable(RefinedVariable vi) {
		ctxSpecificVars.add(vi);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("###########Variables############");
		for(List<RefinedVariable> l : ctxVars) {
			sb.append("{");
			for(RefinedVariable var: l) {
				sb.append(var.toString()+"; ");
			}
			sb.append("}\n");
		}
		sb.append("\n############Functions:############\n");
		for(RefinedFunction f : ctxFunctions)
			sb.append(f.toString());
		return sb.toString();
	}



}