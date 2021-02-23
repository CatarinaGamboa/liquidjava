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
	
	private List<RefinedVariable> ctxGlobalVars;
	private List<RefinedFunction> ctxGlobalFunctions;
	
	private List<GhostFunction> ghosts;
	private List<AliasWrapper> alias;
	

	public int counter;
	private static Context instance;


	private Context() {
		ctxVars = new Stack<>();
		ctxVars.add(new ArrayList<>());
		ctxFunctions = new ArrayList<>();
		ctxSpecificVars = new ArrayList<>();
		//globals
		ctxGlobalVars = new ArrayList<>();
		ctxGlobalFunctions = new ArrayList<>();
		
		alias = new ArrayList<>();
		ghosts = new ArrayList<>();
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
		alias = new ArrayList<>();
		ghosts = new ArrayList<>();
		counter = 0;
	}

	public void enterContext() {
		ctxVars.push(new ArrayList<>());
		//make each variable enter context
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).enterContext();

	}

	public void exitContext() {
		ctxVars.pop();
		//make each variable exit context
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).exitContext();
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
		for(RefinedVariable var: ctxGlobalVars)
			ret.put(var.getName(), var.getType());
		return ret;
	}
	
	public void addGlobalVariableToContext(String simpleName, CtTypeReference<?> type, Constraint c) {
		RefinedVariable vi = new Variable(simpleName, type, c);
		ctxGlobalVars.add(vi);
	}
	
	public void addGlobalVariableToContext(String simpleName, String location,
			CtTypeReference<?> type, Constraint c) {
		RefinedVariable vi = new Variable(simpleName, location, type, c);
		ctxGlobalVars.add(vi);
	}

	public void addVarToContext(RefinedVariable var) {
		//if(!hasVariable(var.getName()))
			ctxVars.peek().add(var);
	}

	public RefinedVariable addVarToContext(String simpleName, CtTypeReference<?> type, Constraint c) {
		RefinedVariable vi = new Variable(simpleName, type, c);
		addVarToContext(vi);
		return vi;
	}
	
	public RefinedVariable addInstanceToContext(String simpleName, CtTypeReference<?> type, Constraint c) {
		RefinedVariable vi = new VariableInstance(simpleName, type, c);
		if(!ctxSpecificVars.contains(vi))
			addSpecificVariable(vi);
		return vi;
	}
	
	public void addRefinementToVariableInContext(String name, CtTypeReference<?> type, Constraint et) {
		if(hasVariable(name)){
			RefinedVariable vi = getVariableByName(name);
			vi.setRefinement(et);
		}else {
			addVarToContext(name, type, et);
		}
	}

	/**
	 * The variable with name variableName will have a new refinement
	 * @param variableName
	 * @param expectedType
	 */
	public void newRefinementToVariableInContext(String variableName, Constraint expectedType) {
		if(hasVariable(variableName)){
			RefinedVariable vi = getVariableByName(variableName);
			vi.setRefinement(expectedType);
		}
	}


	public Constraint getVariableRefinements(String varName) {
		return hasVariable(varName)?getVariableByName(varName).getRefinement() : null; 
	}

	public void variablesSetBeforeIf() {
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).saveInstanceBeforeIf();
	}
	public void variablesSetThenIf() {
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).saveInstanceThen();
	}
	public void variablesSetElseIf() {
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).saveInstanceElse();
	}

	public void variablesNewIfCombination() {
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).newIfCombination();
		
	}

	public void variablesFinishIfCombination() {
		for(RefinedVariable vi: getAllVariables())
			if(vi instanceof Variable)
				((Variable)vi).finishIfCombination();
	}

	public void variablesCombineFromIf(Constraint cond) {
		for(RefinedVariable vi: getAllVariables()) {
			if(vi instanceof Variable) {
				Optional<VariableInstance>ovi = 
						((Variable) vi).getIfInstanceCombination(getCounter(), cond);
				if(ovi.isPresent()) {
					RefinedVariable vii = ovi.get();
					addVarToContext(vii);
					addRefinementInstanceToVariable(vi.getName(), vii.getName());

				}
			}
		}
	}



	public void addFunctionToContext(RefinedFunction f) {
		if(!ctxFunctions.contains(f))
			ctxFunctions.add(f);
	}
	public void addGlobalFunctionToContext(RefinedFunction f) {
		if(!ctxGlobalFunctions.contains(f))
			ctxGlobalFunctions.add(f);
	}

	public RefinedFunction getFunctionByName(String name) {
		for(RefinedFunction fi: ctxFunctions) {
			if(fi.getName().equals(name))
				return fi;
		}
		for(RefinedFunction fi: ctxGlobalFunctions) {
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
		for(RefinedVariable var: ctxGlobalVars) {
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
		RefinedVariable vi1 = getVariableByName(name);
		RefinedVariable vi2 = getVariableByName(name2);
		if(!hasVariable(name) || !hasVariable(name2) || 
				!(vi1 instanceof Variable && vi2 instanceof VariableInstance)) return;
		
		((Variable)vi1).addInstance((VariableInstance) vi2);
		addSpecificVariable(vi2);
	}

	public Optional<VariableInstance> getLastVariableInstance(String name) {
		RefinedVariable rv = getVariableByName(name);
		if(!hasVariable(name) || !(rv instanceof Variable)) return Optional.empty();
		return ((Variable)rv).getLastInstance();
	}

	public void addSpecificVariable(RefinedVariable vi) {
		ctxSpecificVars.add(vi);
	}
	

	public void addGhostFunction(GhostFunction gh) {
		ghosts.add(gh);
	}
	
	public boolean hasGhost(String name) {
		for(GhostFunction g: ghosts) {
			if(g.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public List<GhostFunction> getGhosts() {
		return ghosts;
	}
	
	public void addAlias(AliasWrapper aw) {
		if(!alias.contains(aw))
			alias.add(aw);
	}
	
	public List<AliasWrapper> getAlias() {
		return alias;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n############Global Variables:############\n");
		for(RefinedVariable f : ctxGlobalVars)
			sb.append(f.toString());
		sb.append("\n###########Variables############");
		for(List<RefinedVariable> l : ctxVars) {
			sb.append("{");
			for(RefinedVariable var: l) {
				sb.append(var.toString()+"; ");
			}
			sb.append("}\n");
		}
		sb.append("\n############Global Functions:############\n");
		for(RefinedFunction f : ctxGlobalFunctions)
			sb.append(f.toString());
		sb.append("\n############Functions:############\n");
		for(RefinedFunction f : ctxFunctions)
			sb.append(f.toString());
		
		sb.append("\n############Ghost Functions:############\n");
		for(GhostFunction f : ghosts)
			sb.append(f.toString());
		return sb.toString();
	}

}
