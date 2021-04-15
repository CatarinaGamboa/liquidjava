package repair.regen.utils;

import java.util.HashMap;
import java.util.List;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.context.PlacementInCode;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ErrorPrinter {


	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param s 
	 * @param expectedType
	 * @param cSMT
	 */
	public static <T> void printError(CtElement var, Constraint expectedType, Constraint cSMT, 
			HashMap<String, PlacementInCode> map) {
		printError(var, null, expectedType, cSMT, map);
	}
	public static <T> void printError(CtElement var, String moreInfo, Constraint expectedType, 
			Constraint cSMT, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		if(moreInfo!=null)
			System.out.println(moreInfo);
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + expectedType.toString());
		System.out.println("Refinement found:" + cSMT.toString());
		printMap(map);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}
	
	public static void printStateMismatch(CtElement element, String method, Constraint c, String states, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println(" Failed to check state transitions when calling "+ method+" in:");
		System.out.println();
		System.out.println(element);
		System.out.println();
		System.out.println("Expected possible states:" + states);
		System.out.println("State found:" + c.toString());
		printMap(map);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);

	}
	
	
	public static <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println("Encountered unknown variable");
		System.out.println();
		System.out.println(var);
		System.out.println();
		printMap(map);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}
	
	public static <T> void printNotFound(CtElement var, Constraint constraint, Constraint constraint2, String msg, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println(msg);
		System.out.println(constraint);
		System.out.println(constraint2);
		System.out.println();
		System.out.println("Error found while checking conditions in:");
		System.out.println(var);
		System.out.println();
		printMap(map);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}
	
	
	public static <T> void printErrorArgs(CtElement var, Constraint expectedType, String msg, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println("Error in ghost invocation: "+ msg);
		System.out.println(var+"\nError in refinement:" + expectedType.toString());
		printMap(map);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
	}

	public static void printErrorTypeMismatch(CtElement element, Constraint expectedType, String message, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println(message);
		System.out.println();
		System.out.println(element);
		printMap(map);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);

	}

	public static void printSameStateSetError(CtElement element, Constraint p,String name, HashMap<String, PlacementInCode> map) {
		System.out.println("______________________________________________________");
		System.err.println(" Error found multiple disjoint states from a State Set in a refinement");
		System.out.println();
		System.out.println(element);
		System.out.println();
		System.out.println("In predicate:" + p.toString());
		System.out.println("In class:" + name);
		printMap(map);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
		
	}

	public static void printErrorConstructorFromState(CtElement element, CtLiteral<String> from) {
		System.out.println("______________________________________________________");
		System.err.println(" Error found constructor with FROM state (Constructor's should only have a TO state)");
		System.out.println();
		System.out.println(element);
		System.out.println();
		System.out.println("State found:" + from);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
		
	}
	
	public static void printCostumeError(CtElement element, String msg) {
		System.out.println("______________________________________________________");
		System.err.println(" Found Error: "+msg);
		System.out.println();
		System.out.println(element);
		System.out.println();
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
		
	}

	
	public static void printSyntaxError(String msg, String ref, CtElement element) {
		System.out.println("______________________________________________________");
		System.err.println("Syntax error with message");
		System.out.println(msg);
		System.out.println("Found in refinement:");
		System.out.println(ref);
		System.out.println("In:");
		System.out.println(element);
		System.out.println("Location: " + element.getPosition());
		System.out.println("______________________________________________________");
		System.exit(2);
		
	}
	
	public static void printSyntaxError(String msg, String ref) {
		System.out.println("______________________________________________________");
		System.err.println("Syntax error with message");
		System.out.println(msg);
		System.out.println("Found in refinement:");
		System.out.println(ref);
		System.out.println("______________________________________________________");
		System.exit(2);
		
	}
	
	private static void printMap(HashMap<String, PlacementInCode> map) {
		System.out.println("\nInstance translation table:");
		for (int i = 0; i < 130; i++) System.out.print("-");
		System.out.format("\n|%-32s | %-60s | %-1s \n", "Variable Name", "Saves the result of", "File");
		for (int i = 0; i < 130; i++) System.out.print("-");
		System.out.println();
		for(String s : map.keySet())
			System.out.format("|%-32s | %-60s | %-1s \n", s, map.get(s).getText(), map.get(s).getSimplePosition());
		
		for (int i = 0; i < 130; i++) System.out.print("-");
		System.out.println();
		
	}
}
