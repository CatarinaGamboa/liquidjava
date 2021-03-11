package repair.regen.processor.refinement_checker;

import repair.regen.processor.constraints.Constraint;
import spoon.reflect.declaration.CtElement;

public class ErrorPrinter {

	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param expectedType
	 * @param cSMT
	 */
	public static <T> void printError(CtElement var, Constraint expectedType, Constraint cSMT) {
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
	
	public static void printStateMismatch(CtElement element, Constraint c, String states) {
		System.out.println("______________________________________________________");
		System.err.println(" Failed to check all state transitions ");
		System.out.println();
		System.out.println(element);
		System.out.println();
		System.out.println("Expected possible states:" + states);
		System.out.println("State found:" + c.toString());
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);

	}
	
	
	public static <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Encountered unknown variable");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}
	public static <T> void printErrorArgs(CtElement var, Constraint expectedType, String msg) {
		System.out.println("______________________________________________________");
		System.err.println("Error in ghost invocation: "+ msg);
		System.out.println(var+"\nError in refinement:" + expectedType.toString());
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}

	public static void printErrorTypeMismatch(CtElement element, Constraint expectedType, String message) {
		System.out.println("______________________________________________________");
		System.err.println(message);
		System.out.println();
		System.out.println(element);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);

	}

}
