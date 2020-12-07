package repair.regen.processor;

import java.util.ArrayList;
import java.util.List;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.declaration.CtElement;

public class VCChecker {
	private Context context;
	private List<List<String>> allVariables;
	private List<String> variables;//pointer for last list of allVariables

	public VCChecker() {
		context = Context.getInstance();
		allVariables = new ArrayList<>();
		allVariables.add(new ArrayList<String>());
	}

	public void renewVariables() {
		variables = new ArrayList<>();
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
	public void enterContext() {
		allVariables.add(new ArrayList<String>());
		variables = allVariables.get(allVariables.size()-1);
	}
	public void exitContext() {
		allVariables.remove(allVariables.size()-1);
		variables = allVariables.get(allVariables.size()-1);
	}

	public void processSubtyping(String expectedType, CtElement element) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();

		for(String var:getVariables()) {
			//			System.out.println("var:"+var);
			VariableInfo vi = context.getVariableByName(var);
			String ref = vi.getRefinement();
			sb.append("forall "+var+":"+ref+" -> ");
			sbSMT.append(sbSMT.length()>0?" && "+ref : ref);
		}
		sb.append(expectedType);
		System.out.println("----------------------------VC--------------------------------");
		System.out.println("VC:"+sb.toString());
		System.out.println("SMT subtyping:" + sbSMT.toString() + " <: " + expectedType);
		System.out.println("--------------------------------------------------------------");
		smtChecking(sbSMT.toString(), expectedType, element);
	}

	private void smtChecking(String correctRefinement, String expectedType, CtElement element) {
		try {
			new SMTEvaluator().verifySubtype(correctRefinement, expectedType, context.getContext());
		} catch (TypeCheckError e) {
			printError(element, expectedType, correctRefinement);

		}

	}

	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param et
	 * @param correctRefinement
	 */
	private <T> void printError(CtElement var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + et);
		System.out.println("Refinement found:" + correctRefinement);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}
}
