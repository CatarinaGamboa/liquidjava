package repair.regen.processor;

import java.util.List;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.declaration.CtElement;

public class VCChecker {
	private Context context;
	public VCChecker() {
		context = Context.getInstance();
	}
	public void processSubtyping(List<String> variables, String expectedType, CtElement element) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbSMT = new StringBuilder();
		for(String var:variables) {
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
