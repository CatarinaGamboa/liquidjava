package repair.regen.processor.refinement_checker;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
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
	private List<List<RefinedVariable>> allVariables;
	private List<RefinedVariable> pathVariables;

	public VCChecker() {
		context = Context.getInstance();
		allVariables = new ArrayList<>();
		allVariables.add(new ArrayList<>());
		pathVariables = new Stack<>();
	}

	public void renewVariables() {
		int size = allVariables.size();
		if(size > 0) {
			allVariables.remove(size-1);
			allVariables.add(new ArrayList<>());
		}
	}
	public void addRefinementVariable(RefinedVariable var) {
		List<RefinedVariable> variables = allVariables.get(allVariables.size()-1);
		if(!variables.contains(var))
			variables.add(var);
	}

	public List<RefinedVariable> getVariables() {
		List<RefinedVariable> all = new ArrayList<>();
		for(List<RefinedVariable> l : allVariables)
			for(RefinedVariable s : l)
				if(!all.contains(s))
					all.add(s);

		return all;
	}

	private void removeFromAllVariables(RefinedVariable s1) {
		for(List<RefinedVariable> l : allVariables)
			if(l.contains(s1))
				l.remove(s1);
	}

	public List<RefinedVariable> getLastContextVariables(){
		return allVariables.get(allVariables.size()-1);
	}
	public void setPathVariables(RefinedVariable rv) {
		pathVariables.add(rv);
	}

	public void removePathVariable(RefinedVariable rv) {
		pathVariables.remove(rv);
	}
	public void removeFreshVariableThatIncludes(String otherVar) {
		//Remove from path
		List<RefinedVariable> toRemove = new ArrayList<>();
		for(RefinedVariable rv:pathVariables) {
			if(rv.getRefinement().getVariableNames().contains(otherVar))
				toRemove.add(rv);
		}
		for(RefinedVariable rv:toRemove) {
			pathVariables.remove(rv);
			removeFromAllVariables(rv);
		}

	}

	public void enterContext() {
		allVariables.add(new ArrayList<>());
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
			List<RefinedVariable> pathRemove = new ArrayList<>();

			for(RefinedVariable rv: pathVariables) {
				if(rv.getRefinement().getVariableNames().contains(name))
					pathRemove.add(rv);
			}

			List<RefinedVariable> toSend = getVariables().stream()
					.filter(a->!pathRemove.contains(a))
					.collect(Collectors.toList());
			process(expectedType, element, toSend);
		}
	}

	private void process(Constraint expectedType, CtElement element, List<RefinedVariable> vars) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();

		Constraint cSMT = new Predicate();
		List<RefinedVariable> vars2 = getAllVariablesInRefinements(vars);
		for(RefinedVariable var:vars2) {
			cSMT = new Conjunction(cSMT, var.getRefinement());
			String ref = var.getRefinement().toString();
			sb.append("forall "+var.getName()+":"+ref+" -> \n");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}
		sb.append(expectedType);
		printVCs(sb.toString(), sbSMT.toString(), expectedType);
		smtChecking(cSMT, expectedType, element);
	}

	private List<RefinedVariable> getAllVariablesInRefinements(List<RefinedVariable> vars) {
		List<RefinedVariable> newVars = new ArrayList();
		for (RefinedVariable var:getVariables()) 
			newVars.add(var);
		for (RefinedVariable var:getVariables())
			recAuxGetVars(var, newVars);
		return newVars;
	}

	private void recAuxGetVars(RefinedVariable var, List<RefinedVariable> newVars) {
		if(!context.hasVariable(var.getName()))
			return;
		Constraint c = var.getRefinement();
		String varName = var.getName();
		List<String> l = c.getVariableNames();
		for(String name:l) {
			if(!name.equals(varName) && context.hasVariable(name)) {
				RefinedVariable rv = context.getVariableByName(name);
				if(!newVars.contains(rv)) { 
					newVars.add(rv);
					recAuxGetVars(rv, newVars);
				}
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
