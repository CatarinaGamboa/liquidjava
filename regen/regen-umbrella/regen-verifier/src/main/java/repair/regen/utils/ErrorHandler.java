package repair.regen.utils;

import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.constraints.Predicate;
import repair.regen.processor.constraints.VCImplication;
import repair.regen.processor.context.PlacementInCode;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ErrorHandler {
	
	ErrorListener el = ErrorListener.getInstance();


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
		StringBuilder sb = new StringBuilder();
		
		sb.append("______________________________________________________\n");
		sb.append("Failed to check refinement at: \n\n");

		if(moreInfo!=null)
			sb.append(moreInfo+"\n");
		sb.append(var + "\n\n");
		sb.append("Type expected:" + expectedType.toString()+"\n");
		sb.append("Refinement found:" + cSMT.toString()+"\n");
		sb.append(printMap(map));
		sb.append("Location: " + var.getPosition()+ "\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(1);
	}
	
	public static void printStateMismatch(CtElement element, String method, VCImplication constraintForErrorMsg, 
			String states, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append(" Failed to check state transitions when calling "+ method+" in:\n\n");
		sb.append(element+"\n\n");
		sb.append("Expected possible states:" + states + "\n");
		sb.append("\nState found:\n");
		//TODO SHOW
		sb.append(printLine());
		sb.append("\n"+constraintForErrorMsg/*.toConjunctions()*/.toString()+"\n");
		sb.append(printLine());
		sb.append("\n");
		sb.append(printMap(map));
		sb.append("Location: " + element.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(1);

	}
	
	
	public static <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append("Encountered unknown variable\n\n");
		sb.append(var+"\n\n");
		sb.append(printMap(map));
		sb.append("Location: " + var.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);
	}
	
	public static <T> void printNotFound(CtElement var, Constraint constraint, Constraint constraint2, String msg, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append(msg);
		sb.append(constraint+"\n");
		sb.append(constraint2+"\n\n");
		sb.append("Error found while checking conditions in:\n");
		sb.append(var+"\n\n");
		sb.append(printMap(map));
		sb.append("Location: " + var.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);
	}
	
	
	public static <T> void printErrorArgs(CtElement var, Constraint expectedType, String msg, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append("Error in ghost invocation: "+ msg+"\n");
		sb.append(var+"\nError in refinement:" + expectedType.toString()+"\n");
		sb.append(printMap(map));
		sb.append("Location: " + var.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);
	}

	public static void printErrorTypeMismatch(CtElement element, Constraint expectedType, String message, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append(message+"\n\n");
		sb.append(element+"\n");
		sb.append(printMap(map));
		sb.append("Location: " + element.getPosition() + "\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);

	}

	public static void printSameStateSetError(CtElement element, Constraint p,String name, HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append("Error found multiple disjoint states from a State Set in a refinement\n\n");
		sb.append(element+"\n\n");
		sb.append("In predicate:" + p.toString()+"\n");
		sb.append("In class:" + name+"\n");
		sb.append(printMap(map));
		sb.append("Location: " + element.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(1);
	}

	public static void printErrorConstructorFromState(CtElement element, CtLiteral<String> from) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append(" Error found constructor with FROM state (Constructor's should only have a TO state)\n\n");
		sb.append(element+"\n\n");
		sb.append("State found:" + from + "\n");
		sb.append("Location: " + element.getPosition() + "\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(1);
		
	}
	
	public static void printCostumeError(CtElement element, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append(" Found Error: "+msg+"\n\n");
		sb.append(element+"\n\n");
		sb.append("Location: " + element.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		sb.append(sb.toString());
		System.out.println(sb.toString());
		System.exit(1);	
	}

	
	public static void printSyntaxError(String msg, String ref, CtElement element) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append("Syntax error with message\n");
		sb.append(msg+"\n");
		sb.append("Found in refinement:\n");
		sb.append(ref+"\n");
		sb.append("In:\n");
		sb.append(element+"\n");
		sb.append("Location: " + element.getPosition()+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);
		
	}
	
	public static void printSyntaxError(String msg, String ref) {
		StringBuilder sb = new StringBuilder();
		sb.append("______________________________________________________\n");
		sb.append("Syntax error with message\n");
		sb.append(msg+"\n");
		sb.append("Found in refinement:\n");
		sb.append(ref+"\n");
		sb.append("______________________________________________________\n");
		System.out.println(sb.toString());
		System.exit(2);
		
	}
	
	private static String printLine() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 130; i++) sb.append("-");//-----------
		return sb.toString();
	}
	
	private static String printMap(HashMap<String, PlacementInCode> map) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		if(map.isEmpty()) return "";
		formatter.format("\nInstance translation table:\n");
		formatter.format(printLine());
		//title
		formatter.format("\n| %-32s | %-60s | %-1s \n", "Variable Name", "Created in", "File");
		formatter.format(printLine()+"\n");
		//data
		for(String s : map.keySet())
			formatter.format("| %-32s | %-60s | %-1s \n", s, map.get(s).getText(), map.get(s).getSimplePosition());
		//end
		formatter.format(printLine()+"\n\n");
		return formatter.toString();
	}
}
