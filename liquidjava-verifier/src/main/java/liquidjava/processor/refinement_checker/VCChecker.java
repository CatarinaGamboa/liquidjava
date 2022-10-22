package liquidjava.processor.refinement_checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.logging.LogElement;
import liquidjava.processor.VCImplication;
import liquidjava.processor.context.*;
import liquidjava.rj_language.Predicate;
import liquidjava.smt.GhostFunctionError;
import liquidjava.smt.NotFoundError;
import liquidjava.smt.SMTEvaluator;
import liquidjava.smt.TypeCheckError;
import liquidjava.smt.TypeMismatchError;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;

public class VCChecker {
    private final Context context;
    private final List<RefinedVariable> pathVariables;
    private final ErrorEmitter errorEmitter;
    Pattern thisPattern = Pattern.compile("#this_\\d+");
    Pattern instancePattern = Pattern.compile("^#(.+)_[0-9]+$");

    public VCChecker(ErrorEmitter errorEmitter) {
        context = Context.getInstance();
        pathVariables = new Stack<>();
        this.errorEmitter = errorEmitter;
    }

    public void processSubtyping(Predicate expectedType, List<GhostState> list, String wild_var, String this_var,
            LogElement element, Factory f) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        if (expectedType.isBooleanTrue())
            return;

        HashMap<String, PlacementInCode> map = new HashMap<>();
        String[] s = { wild_var, this_var };
        Predicate premisesBeforeChange = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
        Predicate premises = new Predicate();
        Predicate et = new Predicate();
        try {
            premises = premisesBeforeChange.changeStatesToRefinements(list, s, errorEmitter)
                    .changeAliasToRefinement(context, f);

            et = expectedType.changeStatesToRefinements(list, s, errorEmitter).changeAliasToRefinement(context, f);
        } catch (Exception e1) {
            printError(premises, expectedType, element, map, e1.getMessage());
            return;
        }

        try {
            smtChecking(premises, et);
        } catch (Exception e) {
            // To emit the message we use the constraints before the alias and state change
            printError(e, premisesBeforeChange, expectedType, element, map);
        }
    }

    public void processSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, String wild_var,
            String this_var, LogElement element, String string, Factory f) {
        boolean b = canProcessSubtyping(type, expectedType, list, wild_var, this_var, element, f);
        if (!b)
            printSubtypingError(element, expectedType, type, string);
    }

    public boolean canProcessSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, String wild_var,
            String this_var, LogElement p, Factory f) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        gatherVariables(type, lrv, mainVars);
        if (expectedType.isBooleanTrue() && type.isBooleanTrue())
            return true;

        // Predicate premises = joinPredicates(type, element, mainVars, lrv);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        String[] s = { wild_var, this_var };

        Predicate premises = new Predicate();
        Predicate et = new Predicate();
        try {
            premises = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
            premises = Predicate.createConjunction(premises, type).changeStatesToRefinements(list, s, errorEmitter)
                    .changeAliasToRefinement(context, f);
            et = expectedType.changeStatesToRefinements(list, s, errorEmitter).changeAliasToRefinement(context, f);
        } catch (Exception e) {
            return false;
            // printError(premises, expectedType, element, map, e.getMessage());
        }

        // System.out.println("premise: " + premises.toString() + "\nexpectation: " +
        // et.toString());
        return smtChecks(premises, et, p);
    }

    private /* Predicate */ VCImplication joinPredicates(Predicate expectedType, List<RefinedVariable> mainVars,
            List<RefinedVariable> vars, HashMap<String, PlacementInCode> map) {

        VCImplication firstSi = null;
        VCImplication lastSi = null;
        // Check
        for (RefinedVariable var : mainVars) { // join main refinements of mainVars
            addMap(var, map);
            VCImplication si = new VCImplication(var.getName(), var.getType(), var.getMainRefinement());
            if (lastSi != null) {
                lastSi.setNext(si);
                lastSi = si;
            }
            if (firstSi == null) {
                firstSi = si;
                lastSi = si;
            }
        }

        for (RefinedVariable var : vars) { // join refinements of vars
            addMap(var, map);
            VCImplication si = new VCImplication(var.getName(), var.getType(), var.getRefinement());
            if (lastSi != null) {
                lastSi.setNext(si);
                lastSi = si;
            }
            if (firstSi == null) {
                firstSi = si;
                lastSi = si;
            }
        }
        VCImplication cSMT = new VCImplication(new Predicate());
        if (firstSi != null && lastSi != null) {
            cSMT = firstSi.clone();
            lastSi.setNext(new VCImplication(expectedType));
            // printVCs(firstSi.toString(), cSMT.toConjunctions().toString(), expectedType);
        }

        return cSMT; // firstSi != null ? firstSi : new VCImplication(new Predicate());
    }

    private void addMap(RefinedVariable var, HashMap<String, PlacementInCode> map) {
        map.put(var.getName(), var.getPlacementInCode());
        // if(var instanceof VariableInstance) {
        // VariableInstance vi = (VariableInstance) var;
        // if(vi.getParent().isPresent())
        // map.put(vi.getName(), vi.getParent().get().getName());
        // else if(instancePattern.matcher(var.getName()).matches()){
        // String result = var.getName().replaceAll("(_[0-9]+)$", "").replaceAll("^#",
        // "");
        // map.put(var.getName(), result);
        // }
        // }else if(thisPattern.matcher(var.getName()).matches())
        // map.put(var.getName(), "this");
    }

    private void gatherVariables(Predicate expectedType, List<RefinedVariable> lrv, List<RefinedVariable> mainVars) {
        for (String s : expectedType.getVariableNames()) {
            if (context.hasVariable(s)) {
                RefinedVariable rv = context.getVariableByName(s);
                if (!mainVars.contains(rv) && !lrv.contains(rv))
                    mainVars.add(rv);
                List<RefinedVariable> lm = getVariables(rv.getMainRefinement(), rv.getName());
                addAllDifferent(lrv, lm, mainVars);
            }
        }
    }

    private void addAllDifferent(List<RefinedVariable> toExpand, List<RefinedVariable> from,
            List<RefinedVariable> remove) {
        from.stream().filter(rv -> !toExpand.contains(rv) && !remove.contains(rv)).forEach(toExpand::add);
        // for (RefinedVariable rv : from) {
        // if (!toExpand.contains(rv) && !remove.contains(rv))
        // toExpand.add(rv);
        // }
    }

    private List<RefinedVariable> getVariables(Predicate c, String varName) {
        List<RefinedVariable> allVars = new ArrayList<>();
        getVariablesFromContext(c.getVariableNames(), allVars, varName);
        List<String> pathNames = pathVariables.stream().map(Refined::getName).collect(Collectors.toList());
        getVariablesFromContext(pathNames, allVars, "");

        return allVars;
    }

    private void getVariablesFromContext(List<String> lvars, List<RefinedVariable> allVars, String notAdd) {
        lvars.stream().filter(name -> !name.equals(notAdd) && context.hasVariable(name)).map(context::getVariableByName)
                .filter(rv -> !allVars.contains(rv)).forEach(rv -> {
                    allVars.add(rv);
                    recAuxGetVars(rv, allVars);
                });
    }

    private void recAuxGetVars(RefinedVariable var, List<RefinedVariable> newVars) {
        if (!context.hasVariable(var.getName()))
            return;
        Predicate c = var.getRefinement();
        String varName = var.getName();
        List<String> l = c.getVariableNames();
        getVariablesFromContext(l, newVars, varName);
    }

    public boolean smtChecks(Predicate cSMT, Predicate expectedType, LogElement p) {
        try {
            new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
        } catch (TypeCheckError e) {
            return false;
        } catch (Exception e) {
            // System.err.println("Unknown error:"+e.getMessage());
            // e.printStackTrace();
            // System.exit(7);
            // fail();
            errorEmitter.addError("Unknown Error", e.getMessage(), p.getPosition(), 7);
        }
        return true;
    }

    /**
     * Checks the expectedType against the cSMT constraint. If the types do not check and error is sent and the program
     * ends
     *
     * @param cSMT
     * @param expectedType
     * 
     * @throws Exception
     * @throws GhostFunctionError
     * @throws TypeCheckError
     */
    private void smtChecking(Predicate cSMT, Predicate expectedType)
            throws TypeCheckError, GhostFunctionError, Exception {
        new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
    }

    /**
     * Change variables in constraint by their value expression in the map
     *
     * @param c
     * @param map
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private Predicate substituteByMap(Predicate c, HashMap<String, String> map) {
        map.keySet().forEach(s -> c.substituteVariable(s, map.get(s)));
        return c;
    }

    public void addPathVariable(RefinedVariable rv) {
        pathVariables.add(rv);
    }

    public void removePathVariable(RefinedVariable rv) {
        pathVariables.remove(rv);
    }

    void removePathVariableThatIncludes(String otherVar) {
        pathVariables.stream().filter(rv -> rv.getRefinement().getVariableNames().contains(otherVar))
                .collect(Collectors.toList()).forEach(pathVariables::remove);
    }

    private void printVCs(String string, String stringSMT, Predicate expectedType) {
        System.out.println("\n----------------------------VC--------------------------------\n");
        System.out.println(string);
        System.out.println("\nSMT subtyping:" + stringSMT + " <: " + expectedType.toString());
        System.out.println("--------------------------------------------------------------");
    }

    // Print
    // Errors---------------------------------------------------------------------------------------------------

    private HashMap<String, PlacementInCode> createMap(LogElement element, Predicate expectedType) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        joinPredicates(expectedType, mainVars, lrv, map);
        return map;
    }

    protected void printSubtypingError(LogElement element, Predicate expectedType, Predicate foundType,
            String customeMsg) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        gatherVariables(foundType, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        Predicate premises = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
        printError(premises, expectedType, element, map, customeMsg);
    }

    public void printSameStateError(LogElement element, Predicate expectedType, String klass) {
        HashMap<String, PlacementInCode> map = createMap(element, expectedType);
        ErrorHandler.printSameStateSetError(element, expectedType, klass, map, errorEmitter);
    }

    private void printError(Exception e, Predicate premisesBeforeChange, Predicate expectedType, LogElement element,
            HashMap<String, PlacementInCode> map) {
        String s = null;
        if (element instanceof CtInvocation) {
            CtInvocation<?> ci = (CtInvocation<?>) element;
            String totalS = ci.getExecutable().toString();
            if (ci.getTarget() != null) {
                int targetL = ci.getTarget().toString().length();
                totalS = ci.toString().substring(targetL + 1);
            }
            s = "Method invocation " + totalS + " in:";
        }

        Predicate etMessageReady = expectedType; // substituteByMap(expectedType, map);
        Predicate cSMTMessageReady = premisesBeforeChange; // substituteByMap(premisesBeforeChange, map);
        if (e instanceof TypeCheckError) {
            ErrorHandler.printError(element, s, etMessageReady, cSMTMessageReady, map, errorEmitter);
        } else if (e instanceof GhostFunctionError) {
            ErrorHandler.printErrorArgs(element, etMessageReady, e.getMessage(), map, errorEmitter);
        } else if (e instanceof TypeMismatchError) {
            ErrorHandler.printErrorTypeMismatch(element, etMessageReady, e.getMessage(), map, errorEmitter);
        } else if (e instanceof NotFoundError) {
            ErrorHandler.printNotFound(element, cSMTMessageReady, etMessageReady, e.getMessage(), map, errorEmitter);
        } else {
            ErrorHandler.printCostumeError(element, e.getMessage(), errorEmitter);
            // System.err.println("Unknown error:"+e.getMessage());
            // e.printStackTrace();
            // System.exit(7);
        }
    }

    private void printError(Predicate premises, Predicate expectedType, LogElement element,
            HashMap<String, PlacementInCode> map, String s) {
        Predicate etMessageReady = expectedType; // substituteByMap(expectedType, map);
        Predicate cSMTMessageReady = premises; // substituteByMap(premises, map);
        ErrorHandler.printError(element, s, etMessageReady, cSMTMessageReady, map, errorEmitter);
    }

    public void printStateMismatchError(LogElement element, String method, Predicate c, String states) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(c, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        VCImplication constraintForErrorMsg = joinPredicates(c, mainVars, lrv, map);
        // HashMap<String, PlacementInCode> map = createMap(element, c);
        ErrorHandler.printStateMismatch(element, method, constraintForErrorMsg, states, map, errorEmitter);
    }
}
