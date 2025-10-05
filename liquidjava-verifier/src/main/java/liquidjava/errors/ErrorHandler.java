package liquidjava.errors;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import liquidjava.processor.VCImplication;
import liquidjava.processor.context.PlacementInCode;
import liquidjava.rj_language.Predicate;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;

public class ErrorHandler {

    /**
     * Prints the error message
     *
     * @param <T>
     * @param var
     * @param s
     * @param expectedType
     * @param cSMT
     */
    public static <T> void printError(CtElement var, Predicate expectedType, Predicate cSMT,
            HashMap<String, PlacementInCode> map, ErrorEmitter ee) {
        printError(var, null, expectedType, cSMT, map, ee);
    }

    public static <T> void printError(CtElement var, String moreInfo, Predicate expectedType, Predicate cSMT,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {
        String resumeMessage = "Type expected:" + expectedType.toString(); // + "; " +"Refinement found:" +
        // cSMT.toString();

        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        // title
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Failed to check refinement at: \n\n");
        if (moreInfo != null)
            sbtitle.append(moreInfo + "\n");
        sbtitle.append(var.toString());
        // all message
        sb.append(sbtitle.toString() + "\n\n");
        sb.append("Type expected:" + expectedType.toString() + "\n");
        sb.append("Refinement found:\n" + cSMT.simplify().getValue() + "\n");
        sb.append(printMap(map));
        sb.append("Location: " + var.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(resumeMessage, sb.toString(), var.getPosition(), 1, map);
    }

    public static void printStateMismatch(CtElement element, String method, VCImplication constraintForErrorMsg,
            String states, HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {

        String resumeMessage = "Failed to check state transitions. " + "Expected possible states:" + states; // + ";
        // Found
        // state:"+constraintForErrorMsg.toString()
        // ;

        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");

        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Failed to check state transitions when calling " + method + " in:\n\n");
        sbtitle.append(element + "\n\n");

        sb.append(sbtitle.toString());
        sb.append("Expected possible states:" + states + "\n");
        sb.append("\nState found:\n");
        sb.append(printLine());
        sb.append("\n" + constraintForErrorMsg /* .toConjunctions() */.toString() + "\n");
        sb.append(printLine());
        sb.append("\n");
        sb.append(printMap(map));
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(resumeMessage, sb.toString(), element.getPosition(), 1, map);
    }

    public static <T> void printErrorUnknownVariable(CtElement var, String et, String correctRefinement,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {

        String resumeMessage = "Encountered unknown variable";

        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Encountered unknown variable\n\n");
        sbtitle.append(var + "\n\n");

        sb.append(sbtitle.toString());
        sb.append(printMap(map));
        sb.append("Location: " + var.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(resumeMessage, sb.toString(), var.getPosition(), 2, map);
    }

    public static <T> void printNotFound(CtElement var, Predicate constraint, Predicate constraint2, String msg,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {

        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        sb.append(msg);
        sb.append(constraint + "\n");
        sb.append(constraint2 + "\n\n");
        sb.append("Error found while checking conditions in:\n");
        sb.append(var + "\n\n");
        sb.append(printMap(map));
        sb.append("Location: " + var.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(msg, sb.toString(), var.getPosition(), 2, map);
    }

    public static <T> void printErrorArgs(CtElement var, Predicate expectedType, String msg,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        String title = "Error in ghost invocation: " + msg + "\n";
        sb.append(title);
        sb.append(var + "\nError in refinement:" + expectedType.toString() + "\n");
        sb.append(printMap(map));
        sb.append("Location: " + var.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(title, sb.toString(), var.getPosition(), 2, map);
    }

    public static void printErrorTypeMismatch(CtElement element, Predicate expectedType, String message,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        sb.append(message + "\n\n");
        sb.append(element + "\n");
        sb.append(printMap(map));
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(message, sb.toString(), element.getPosition(), 2, map);
    }

    public static void printSameStateSetError(CtElement element, Predicate p, String name,
            HashMap<String, PlacementInCode> map, ErrorEmitter errorl) {
        String resume = "Error found multiple disjoint states from a State Set in a refinement";

        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Error found multiple disjoint states from a State Set in a refinement\n\n");
        sbtitle.append(element + "\n\n");
        sb.append(sbtitle.toString());
        sb.append("In predicate:" + p.toString() + "\n");
        sb.append("In class:" + name + "\n");
        sb.append(printMap(map));
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(resume, sb.toString(), element.getPosition(), 1, map);
    }

    public static void printErrorConstructorFromState(CtElement element, CtLiteral<String> from, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        String s = " Error found constructor with FROM state (Constructor's should only have a TO state)\n\n";
        sb.append(s);
        sb.append(element + "\n\n");
        sb.append("State found:" + from + "\n");
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(s, sb.toString(), element.getPosition(), 1);
    }

    public static void printCostumeError(CtElement element, String msg, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        String s = "Found Error: " + msg;
        sb.append(s + "\n\n");
        sb.append(element + "\n\n");
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(s, sb.toString(), element.getPosition(), 1);
    }

    public static void printSyntaxError(String msg, String ref, CtElement element, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Syntax error with message\n");
        sbtitle.append(msg + "\n");
        sb.append(sbtitle.toString());
        sb.append("Found in refinement:\n");
        sb.append(ref + "\n");
        sb.append("In:\n");
        sb.append(element + "\n");
        sb.append("Location: " + element.getPosition() + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(sbtitle.toString(), sb.toString(), element.getPosition(), 2);
    }

    public static void printSyntaxError(String msg, String ref, ErrorEmitter errorl) {
        StringBuilder sb = new StringBuilder();
        sb.append("______________________________________________________\n");
        StringBuilder sbtitle = new StringBuilder();
        sbtitle.append("Syntax error with message\n");
        sbtitle.append(msg + "\n");
        sb.append(sbtitle.toString());
        sb.append("Found in refinement:\n");
        sb.append(ref + "\n");
        sb.append("______________________________________________________\n");

        errorl.addError(sbtitle.toString(), sb.toString(), 2);
    }

    private static String printLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 130; i++)
            sb.append("-"); // -----------
        return sb.toString();
    }

    private static String printMap(HashMap<String, PlacementInCode> map) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        if (map.isEmpty()) {
            formatter.close();
            return "";
        }
        formatter.format("\nInstance translation table:\n");
        formatter.format(printLine());
        // title
        formatter.format("\n| %-32s | %-60s | %-1s \n", "Variable Name", "Created in", "File");
        formatter.format(printLine() + "\n");
        // data
        for (String s : map.keySet())
            formatter.format("| %-32s | %-60s | %-1s \n", s, map.get(s).getText(), map.get(s).getSimplePosition());
        // end
        formatter.format(printLine() + "\n\n");
        String s = formatter.toString();
        formatter.close();
        return s;
    }
}
