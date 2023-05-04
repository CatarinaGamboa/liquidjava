package liquidjava.processor.refinement_checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.processor.VCImplication;
import liquidjava.processor.context.*;
import liquidjava.processor.heap.HeapContext;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.smt.GhostFunctionError;
import liquidjava.smt.NotFoundError;
import liquidjava.smt.SMTEvaluator;
import liquidjava.smt.TypeCheckError;
import liquidjava.smt.TypeMismatchError;
import spoon.reflect.code.CtInvocation;
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

    public boolean doesHeapHold(Predicate booleanCtx, HeapContext curHeap, CtElement element) {

        Predicate heaplets = curHeap.toSeparateHeaplets().stream()
                .map(h -> Predicate.createSepConjunction(Predicate.booleanTrue(), h))
                .reduce(Predicate.booleanTrue(), Predicate::createConjunction);

        // `heaplets` is a proof that individual heaplets are pointing to whatever they are pointing to
        // but not a proof that they are valid together.

        Predicate premises = Predicate.createConjunction(booleanCtx, heaplets);

        Predicate target = curHeap.toSepConjunctions();

        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();

        gatherVariables(premises, lrv, mainVars);

        HashMap<String, PlacementInCode> map = new HashMap<>();
        // heap predicate is not in refinements, so it should be added separately
        premises = Predicate.createConjunction(heaplets,
                joinPredicates(booleanCtx, element, mainVars, lrv, map).toConjunctions());

        try {
            // is it true that if individual heaplets are pointing to whaterver they are pointing,
            // heap holds together?
            smtChecking(premises, target, element);
            return true;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Heap does not hold:");
            System.out.println(booleanCtx + " && " + curHeap);
            return false;
        }
    }

    public Predicate reduceHeapKnowledge(Predicate booleanCtx, HeapContext curHeapCtx, CtElement element) {
        return curHeapCtx.toSeparateHeaplets().stream().reduce(Predicate.emptyHeap(), (heap, heaplet) -> {
            Predicate updHeap = Predicate.createSepConjunction(heap, heaplet);
            try {
                if (doesHeapHold(booleanCtx, HeapContext.fromPredicate(updHeap), element)) {
                    System.out.println("Heaplet accepted: " + heaplet);
                    return updHeap;
                } else {
                    System.out.println("Heaplet rejected: " + heaplet);
                    return heap;
                }
            } catch (ParsingException e) {
                System.out.println("Error while reconstructing heap: " + updHeap);
                return heap;
            }
        });
    }

    public void checkHeapPrecondition(Predicate booleanCtx, Predicate curHeap, HeapContext.Transition tr,
            CtElement element) {
        Predicate premises = Predicate.createConjunction(booleanCtx, curHeap);

        Predicate target = Predicate.createSepConjunction(tr.getPre().toSepConjunctions(), Predicate.booleanTrue());

        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();

        gatherVariables(premises, lrv, mainVars);
        gatherVariables(target, lrv, mainVars);

        HashMap<String, PlacementInCode> map = new HashMap<>();
        // heap predicate is not in refinements, so it should be added separately
        premises = Predicate.createConjunction(curHeap,
                joinPredicates(booleanCtx, element, mainVars, lrv, map).toConjunctions());

        try {
            smtChecking(premises, target, element);
        } catch (Exception e) {
            System.out.println();
            printError(e, premises, target, element, map);
        }
    }

    /**
     * The goal is to filter pointers from current heap H to find H1 such that: H = Pre * H1 Then it is sufficient to
     * just swap `Pre` with `Post` by frame rule such that: Res = Post * H1
     * 
     * @param booleanCtx
     *            used to determine which pointers are equal
     * @param curHeap
     *            current heap
     * @param tr
     *            current transition
     * @param element
     *            Spoon's representation of piece of code we currently in
     * 
     * @return transformed heap
     */
    public Predicate applyHeapTransition(Predicate booleanCtx, HeapContext curHeap, HeapContext.Transition tr,
            CtElement element) {
        return curHeap.toSeparateHeaplets().stream().filter(h -> {
            Predicate heap = Predicate.createSepConjunction(tr.getPre().toSepConjunctions(), h);
            Predicate premises = Predicate.createConjunction(booleanCtx, heap);

            List<RefinedVariable> lrv = new ArrayList<>();
            List<RefinedVariable> mainVars = new ArrayList<>();
            gatherVariables(premises, lrv, mainVars);
            HashMap<String, PlacementInCode> map = new HashMap<>();

            premises = Predicate.createConjunction(heap,
                    joinPredicates(booleanCtx, element, mainVars, lrv, map).toConjunctions());

            try {
                smtCheckSAT(premises, element);// to verify that it is possible to have premises
                return true;
            } catch (TypeCheckError e) {
                return false;
            } catch (Exception e) {
                printError(e, premises, Predicate.booleanFalse(), element, map);
                return false;
            }
        }).reduce(tr.getPost().toSepConjunctions(), Predicate::createSepConjunction);

    }

    public void processSubtyping(Predicate expectedType, List<GhostState> list, String wild_var, String this_var,
            CtElement element, Factory f) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        if (expectedType.isBooleanTrue())
            return;

        HashMap<String, PlacementInCode> map = new HashMap<>();
        String[] s = { wild_var, this_var };
        // I think, that here the whole context is assembled to verify the implication.
        // expected type is the result of an implication. The premises here are the premise in said implication.
        // Linked list in VCImplication is to mimic the structure of implication: `a -> b -> c -> expectedType`.

        Predicate premisesBeforeChange = joinPredicates(expectedType, element, mainVars, lrv, map).toConjunctions();
        Predicate premises = Predicate.booleanTrue();
        Predicate et = Predicate.booleanTrue();
        try {
            premises = premisesBeforeChange.changeStatesToRefinements(list, s, errorEmitter)
                    .changeAliasToRefinement(context, element, f);

            et = expectedType.changeStatesToRefinements(list, s, errorEmitter).changeAliasToRefinement(context, element,
                    f);
        } catch (Exception e1) {
            printError(premises, expectedType, element, map, e1.getMessage());
            return;
        }

        // System.out.println(premises.toString() + "\n"+et.toString());
        try {
            smtChecking(premises, et, element);
        } catch (Exception e) {
            // To emit the message we use the constraints before the alias and state change
            System.out.println();
            printError(e, premisesBeforeChange, expectedType, element, map);
        }
    }

    public void processSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, String wild_var,
            String this_var, CtElement element, String string, Factory f) {
        boolean b = canProcessSubtyping(type, expectedType, list, wild_var, this_var, element, f);
        if (!b)
            printSubtypingError(element, expectedType, type, string);
    }

    public boolean canProcessSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, String wild_var,
            String this_var, CtElement element, Factory f) {
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
            premises = joinPredicates(expectedType, element, mainVars, lrv, map).toConjunctions();
            premises = Predicate.createConjunction(premises, type).changeStatesToRefinements(list, s, errorEmitter)
                    .changeAliasToRefinement(context, element, f);
            et = expectedType.changeStatesToRefinements(list, s, errorEmitter).changeAliasToRefinement(context, element,
                    f);
        } catch (Exception e) {
            return false;
            // printError(premises, expectedType, element, map, e.getMessage());
        }

        System.out.println("premise: " + premises.toString() + "\nexpectation: " + et.toString());
        return smtChecks(premises, et, element);
    }

    private /* Predicate */VCImplication joinPredicates(Predicate expectedType, CtElement element,
            List<RefinedVariable> mainVars, List<RefinedVariable> vars, HashMap<String, PlacementInCode> map) {

        VCImplication firstSi = null;
        VCImplication lastSi = null;
        // Check
        for (RefinedVariable var : mainVars) {// join main refinements of mainVars
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

        for (RefinedVariable var : vars) {// join refinements of vars
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
            printVCs(firstSi.toString(), cSMT.toConjunctions().toString(), expectedType);
        }

        return cSMT;// firstSi != null ? firstSi : new VCImplication(new Predicate());
    }

    private void addMap(RefinedVariable var, HashMap<String, PlacementInCode> map) {
        map.put(var.getName(), var.getPlacementInCode());
        // System.out.println();
        // if(var instanceof VariableInstance) {
        // VariableInstance vi = (VariableInstance) var;
        // if(vi.getParent().isPresent())
        // map.put(vi.getName(), vi.getParent().get().getName());
        // else if(instancePattern.matcher(var.getName()).matches()){
        // String result = var.getName().replaceAll("(_[0-9]+)$", "").replaceAll("^#", "");
        // map.put(var.getName(), result);
        // }
        // }else if(thisPattern.matcher(var.getName()).matches())
        // map.put(var.getName(), "this");
    }

    // TODO(ask what is this)
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

    private static void addAllDifferent(List<RefinedVariable> toExpand, List<RefinedVariable> from,
            List<RefinedVariable> remove) {
        from.stream().filter(rv -> !toExpand.contains(rv) && !remove.contains(rv)).forEach(toExpand::add);
    }

    private List<RefinedVariable> getVariables(Predicate c, String varName) {
        List<RefinedVariable> allVars = new ArrayList<>();
        getVariablesFromContext(c.getVariableNames(), allVars, varName);
        List<String> pathNames = pathVariables.stream().map(Refined::getName).collect(Collectors.toList());
        getVariablesFromContext(pathNames, allVars, "");

        return allVars;
    }

    // TODO(ask what is this)
    private void getVariablesFromContext(List<String> lvars, List<RefinedVariable> allVars, String notAdd) {
        lvars.stream().filter(name -> !name.equals(notAdd) && context.hasVariable(name)).map(context::getVariableByName)
                .filter(rv -> !allVars.contains(rv)).forEach(rv -> {
                    allVars.add(rv);
                    recAuxGetVars(rv, allVars);
                });
    }

    // TODO(ask what is this)
    private void recAuxGetVars(RefinedVariable var, List<RefinedVariable> newVars) {
        if (!context.hasVariable(var.getName()))
            return;
        Predicate c = var.getRefinement();
        String varName = var.getName();
        List<String> l = c.getVariableNames();
        getVariablesFromContext(l, newVars, varName);
    }

    public boolean smtChecks(Predicate cSMT, Predicate expectedType, CtElement elem) {
        try {
            new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
        } catch (TypeCheckError e) {
            return false;
        } catch (Exception e) {
            // System.err.println("Unknown error:"+e.getMessage());
            // e.printStackTrace();
            // System.exit(7);
            // fail();
            errorEmitter.addError("Unknown Error", e.getMessage(), elem.getPosition(), 7);
        }
        return true;
    }

    /**
     * Checks the expectedType against the cSMT constraint. If the types do not check and error is sent and the program
     * ends
     *
     * @param cSMT
     * @param expectedType
     * @param element
     *
     * @throws Exception
     * @throws GhostFunctionError
     * @throws TypeCheckError
     */
    private void smtChecking(Predicate cSMT, Predicate expectedType, CtElement element)
            throws TypeCheckError, GhostFunctionError, Exception {
        new SMTEvaluator().verifySubtype(cSMT, expectedType, context);
    }

    private void smtCheckSAT(Predicate cSMT, CtElement element) throws TypeCheckError, GhostFunctionError, Exception {
        new SMTEvaluator().checkIfSAT(cSMT, context);
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
        map.keySet().forEach(s -> c.makeSubstitution(s, map.get(s)));
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

    // Print Errors---------------------------------------------------------------------------------------------------

    private HashMap<String, PlacementInCode> createMap(CtElement element, Predicate expectedType) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        joinPredicates(expectedType, element, mainVars, lrv, map);
        return map;
    }

    protected void printSubtypingError(CtElement element, Predicate expectedType, Predicate foundType,
            String customeMsg) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        gatherVariables(foundType, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        Predicate premises = joinPredicates(expectedType, element, mainVars, lrv, map).toConjunctions();
        printError(premises, expectedType, element, map, customeMsg);
    }

    public void printSameStateError(CtElement element, Predicate expectedType, String klass) {
        HashMap<String, PlacementInCode> map = createMap(element, expectedType);
        ErrorHandler.printSameStateSetError(element, expectedType, klass, map, errorEmitter);
    }

    private void printError(Exception e, Predicate premisesBeforeChange, Predicate expectedType, CtElement element,
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
            System.out.println();
        }

        Predicate etMessageReady = expectedType;// substituteByMap(expectedType, map);
        Predicate cSMTMessageReady = premisesBeforeChange;// substituteByMap(premisesBeforeChange, map);
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

    private void printError(Predicate premises, Predicate expectedType, CtElement element,
            HashMap<String, PlacementInCode> map, String s) {
        Predicate etMessageReady = expectedType;// substituteByMap(expectedType, map);
        Predicate cSMTMessageReady = premises;// substituteByMap(premises, map);
        ErrorHandler.printError(element, s, etMessageReady, cSMTMessageReady, map, errorEmitter);
    }

    public void printStateMismatchError(CtElement element, String method, Predicate c, String states) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(c, lrv, mainVars);
        HashMap<String, PlacementInCode> map = new HashMap<>();
        VCImplication constraintForErrorMsg = joinPredicates(c, element, mainVars, lrv, map);
        // HashMap<String, PlacementInCode> map = createMap(element, c);
        ErrorHandler.printStateMismatch(element, method, constraintForErrorMsg, states, map, errorEmitter);
    }

}
