package repair.regen.processor.refinement_checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.Context;
import repair.regen.processor.context.RefinedVariable;
import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.declaration.CtElement;

public class VCChecker {
	private Context context;
	private List<List<String>> allVariables;
	private Map<String, List<String>> pathVariables;

	public VCChecker() {
		context = Context.getInstance();
		allVariables = new ArrayList<>();
		allVariables.add(new ArrayList<String>());
		pathVariables = new HashMap<>();
	}

	public void renewVariables() {
		int size = allVariables.size();
		if(size > 0) {
			allVariables.remove(size-1);
			allVariables.add(new ArrayList<>());
		}
	}
	public void addRefinementVariable(String varName) {
		List<String> variables = allVariables.get(allVariables.size()-1);
		if(!variables.contains(varName))
			variables.add(varName);
	}
	
	public List<String> getVariables() {
		List<String> all = new ArrayList<>();
		for(List<String> l : allVariables)
			for(String s : l)
				if(!all.contains(s))
					all.add(s);
		
		return all;
	}
	
	private void removeFromAllVariables(String s1) {
		for(List<String> l : allVariables)
			if(l.contains(s1))
				l.remove(s1);
	}

	public List<String> getLastContextVariables(){
		return allVariables.get(allVariables.size()-1);
	}
	public void setPathVariables(String key) {
		pathVariables.put(key, allVariables.get(allVariables.size()-1));
	}
	
	public void removePathVariable(String key) {
		pathVariables.remove(key);
	}
	public void removeFreshVariableThatIncludes(String otherVar) {
		//Remove from path
		List<String> toRemove = new ArrayList<>();
		for(Entry<String, List<String>> e : pathVariables.entrySet()) {
			String pathName = e.getKey();
			for(String s: e.getValue()) 
				if(s.equals(otherVar) && !toRemove.contains(pathName)) 
					toRemove.add(pathName);
		}
		for(String s:toRemove) {
			pathVariables.remove(s);
			removeFromAllVariables(s);
		}
		
	}

	public void enterContext() {
		allVariables.add(new ArrayList<String>());
	}
	public void exitContext() {
		allVariables.remove(allVariables.size()-1);
	}
	

	public void processSubtyping(Constraint expectedType, CtElement element) {
		process(expectedType, element, getVariables());
	}

	public void processSubtyping(Constraint expectedType, String name, CtElement element) {
		if(pathVariables.isEmpty())
			process(expectedType, element, getVariables());
		else {
			List<String> pathRemove = new ArrayList<>();
			for(String k:pathVariables.keySet()) 
				for(String s: pathVariables.get(k))
					if(s.equals(name))
						pathRemove.add(k);
			
			List<String> toSend = getVariables().stream()
					.filter(a->!pathRemove.contains(a))
					.collect(Collectors.toList());
			process(expectedType, element, toSend);
		}
	}

	private void process(Constraint expectedType, CtElement element, List<String> vars) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();

		Constraint cSMT = new Predicate();
		List<String> vars2 = getAllVariablesInRefinements(vars);
		for(String var:vars2) {
			RefinedVariable vi = context.getVariableByName(var);
			if(vi != null) {
				cSMT = new Conjunction(cSMT, vi.getRefinement());
				String ref = vi.getRefinement().toString();
				sb.append("forall "+var+":"+ref+" -> \n");
				sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
			}
		}
		sb.append(expectedType);
		printVCs(sb.toString(), sbSMT.toString(), expectedType);
		smtChecking(cSMT, expectedType, element);
	}

	private List<String> getAllVariablesInRefinements(List<String> vars) {
		List<String> newVars = new ArrayList();
		for (String var:getVariables()) 
			newVars.add(var);
		for (String var:getVariables())
			recAuxGetVars(var, newVars);
		return newVars;
	}

	private void recAuxGetVars(String varName, List<String> newVars) {
		if(!context.hasVariable(varName))
			return;
		Constraint c = context.getVariableRefinements(varName);
		List<String> l = c.getVariableNames();
		for(String name:l) {
			if(!name.equals(varName) && !newVars.contains(name)) {
				newVars.add(name);
				recAuxGetVars(name, newVars);
			}
		}
	}

	private void printVCs(String string, String stringSMT, Constraint expectedType) {
		System.out.println("----------------------------VC--------------------------------");
		System.out.println("VC:"+string);
		System.out.println("SMT subtyping:" + stringSMT + " <: " + expectedType.toString());
		System.out.println("--------------------------------------------------------------");

	}

	private void smtChecking(Constraint cSMT, Constraint expectedType, CtElement element) {
		try {
			new SMTEvaluator().verifySubtype(cSMT.toString(), expectedType.toString(), context.getContext());
		} catch (TypeCheckError e) {
			printError(element, expectedType, cSMT);

		}//catch(NullPointerException e) {
		//	printErrorUnknownVariable(element, expectedType, correctRefinement);
		//}

	}

	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param expectedType
	 * @param cSMT
	 */
	private <T> void printError(CtElement var, Constraint expectedType, Constraint cSMT) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + expectedType.toString());
		System.out.println("Refinement found:" + cSMT.toString());
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}
	private <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Encountered unknown variable");
		System.out.println();
		System.out.println(var);
		System.out.println();
		//System.out.println("Type expected:" + et);
		//System.out.println("Refinement found:" + correctRefinement);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}
}
