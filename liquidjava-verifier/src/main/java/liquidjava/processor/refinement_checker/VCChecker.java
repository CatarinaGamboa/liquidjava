package liquidjava.processor.refinement_checker;

import static liquidjava.diagnostics.LJDiagnostics.diagnostics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import liquidjava.diagnostics.errors.CustomError;
import liquidjava.diagnostics.errors.GhostInvocationError;
import liquidjava.diagnostics.errors.LJError;
import liquidjava.diagnostics.errors.NotFoundError;
import liquidjava.diagnostics.TranslationTable;
import liquidjava.diagnostics.errors.RefinementError;
import liquidjava.diagnostics.errors.StateConflictError;
import liquidjava.diagnostics.errors.StateRefinementError;
import liquidjava.processor.VCImplication;
import liquidjava.processor.context.*;
import liquidjava.rj_language.Predicate;
import liquidjava.smt.GhostFunctionError;
import liquidjava.smt.NotFoundSMTError;
import liquidjava.smt.SMTEvaluator;
import liquidjava.smt.TypeCheckError;
import liquidjava.utils.constants.Keys;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class VCChecker {
    private final Context context;
    private final List<RefinedVariable> pathVariables;

    public VCChecker() {
        context = Context.getInstance();
        pathVariables = new Stack<>();
    }

    public void processSubtyping(Predicate expectedType, List<GhostState> list, CtElement element, Factory f) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        if (expectedType.isBooleanTrue())
            return;

        TranslationTable map = new TranslationTable();
        String[] s = { Keys.WILDCARD, Keys.THIS };
        Predicate premisesBeforeChange = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
        Predicate premises = new Predicate();
        Predicate et = new Predicate();
        try {
            List<GhostState> filtered = filterGhostStatesForVariables(list, mainVars, lrv);
            premises = premisesBeforeChange.changeStatesToRefinements(filtered, s).changeAliasToRefinement(context, f);

            et = expectedType.changeStatesToRefinements(filtered, s).changeAliasToRefinement(context, f);
        } catch (Exception e) {
            diagnostics.add(new RefinementError(element, expectedType, premises.simplify(), map));
            return;
        }

        try {
            smtChecking(premises, et, element.getPosition());
        } catch (Exception e) {
            // To emit the message we use the constraints before the alias and state change
            printError(e, premisesBeforeChange, expectedType, element, map);
        }
    }

    public void processSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, CtElement element,
            String string, Factory f) {
        boolean b = canProcessSubtyping(type, expectedType, list, element.getPosition(), f);
        if (!b)
            printSubtypingError(element, expectedType, type, string);
    }

    public boolean canProcessSubtyping(Predicate type, Predicate expectedType, List<GhostState> list, SourcePosition p,
            Factory f) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        gatherVariables(type, lrv, mainVars);
        if (expectedType.isBooleanTrue() && type.isBooleanTrue())
            return true;

        // Predicate premises = joinPredicates(type, element, mainVars, lrv);
        TranslationTable map = new TranslationTable();
        String[] s = { Keys.WILDCARD, Keys.THIS };

        Predicate premises = new Predicate();
        Predicate et = new Predicate();
        try {
            premises = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
            List<GhostState> filtered = filterGhostStatesForVariables(list, mainVars, lrv);
            premises = Predicate.createConjunction(premises, type).changeStatesToRefinements(filtered, s)
                    .changeAliasToRefinement(context, f);
            et = expectedType.changeStatesToRefinements(filtered, s).changeAliasToRefinement(context, f);
        } catch (Exception e) {
            return false;
        }
        return smtChecks(premises, et, p);
    }

    /**
     * Reduce the ghost states list to those whose declaring class (prefix) matches any of the involved variable types
     * or their supertypes This prevents ambiguous simple name substitutions across unrelated classes that share state
     * names
     */
    private List<GhostState> filterGhostStatesForVariables(List<GhostState> list, List<RefinedVariable> mainVars,
            List<RefinedVariable> vars) {
        if (list.isEmpty())
            return list;

        // Collect all relevant qualified type names from involved variables and their supertypes
        if (list == null || list.isEmpty())
            return list;

        // Collect all relevant qualified type names (types + supertypes), keeping order and deduping
        Set<String> allowedPrefixes = new java.util.LinkedHashSet<>();
        Consumer<RefinedVariable> collect = rv -> {
            if (rv.getType() != null) {
                allowedPrefixes.add(rv.getType().getQualifiedName());
            }
            for (CtTypeReference<?> st : rv.getSuperTypes()) {
                if (st != null) {
                    allowedPrefixes.add(st.getQualifiedName());
                }
            }
        };
        mainVars.forEach(collect);
        vars.forEach(collect);

        if (allowedPrefixes.isEmpty())
            return list; // avoid over-filtering when types are unknown

        List<GhostState> filtered = list.stream().filter(g -> {
            String prefix = (g.getParent() != null) ? g.getParent().getPrefix() : g.getPrefix();
            return allowedPrefixes.contains(prefix);
        }).collect(Collectors.toList());

        // If nothing matched, keep original to avoid accidental empties
        return filtered.isEmpty() ? list : filtered;
    }

    private VCImplication joinPredicates(Predicate expectedType, List<RefinedVariable> mainVars,
            List<RefinedVariable> vars, TranslationTable map) {

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
            // printVCs(firstSi.toString(), cSMT.toConjunctions().toString(), expectedType); //DEBUG: UNCOMMENT
        }

        return cSMT; // firstSi != null ? firstSi : new VCImplication(new Predicate());
    }

    private void addMap(RefinedVariable var, TranslationTable map) {
        map.put(var.getName(), var.getPlacementInCode());
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

    public boolean smtChecks(Predicate cSMT, Predicate expectedType, SourcePosition p) {
        try {
            new SMTEvaluator().verifySubtype(cSMT, expectedType, context, p);
        } catch (TypeCheckError e) {
            return false;
        } catch (Exception e) {
            diagnostics.add(new CustomError(e.getMessage(), p));
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
    private void smtChecking(Predicate cSMT, Predicate expectedType, SourcePosition p)
            throws TypeCheckError, GhostFunctionError, Exception {
        new SMTEvaluator().verifySubtype(cSMT, expectedType, context, p);
    }

    /**
     * Change variables in constraint by their value expression in the map
     *
     * @param c
     * @param map
     *
     * @return
     */
    // private Predicate substituteByMap(Predicate c, HashMap<String, String> map) {
    // map.keySet().forEach(s -> c.substituteVariable(s, map.get(s)));
    // return c;
    // }

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

    // Errors---------------------------------------------------------------------------------------------------

    private TranslationTable createMap(CtElement element, Predicate expectedType) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        TranslationTable map = new TranslationTable();
        joinPredicates(expectedType, mainVars, lrv, map);
        return map;
    }

    protected void printSubtypingError(CtElement element, Predicate expectedType, Predicate foundType,
            String customeMsg) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(expectedType, lrv, mainVars);
        gatherVariables(foundType, lrv, mainVars);
        TranslationTable map = new TranslationTable();
        Predicate premises = joinPredicates(expectedType, mainVars, lrv, map).toConjunctions();
        diagnostics.add(new RefinementError(element, expectedType, premises.simplify(), map));
    }

    public void printSameStateError(CtElement element, Predicate expectedType, String klass) {
        TranslationTable map = createMap(element, expectedType);
        diagnostics.add(new StateConflictError(element, expectedType, klass, map));
    }

    private void printError(Exception e, Predicate premisesBeforeChange, Predicate expectedType, CtElement element,
            TranslationTable map) {
        LJError error = mapError(e, premisesBeforeChange, expectedType, element, map);
        diagnostics.add(error);
    }

    private LJError mapError(Exception e, Predicate premisesBeforeChange, Predicate expectedType, CtElement element,
            TranslationTable map) {
        if (e instanceof TypeCheckError) {
            return new RefinementError(element, expectedType, premisesBeforeChange.simplify(), map);
        } else if (e instanceof GhostFunctionError) {
            return new GhostInvocationError("Invalid types or number of arguments in ghost invocation",
                    element.getPosition(), expectedType, map);
        } else if (e instanceof NotFoundSMTError) {
            return new NotFoundError(element, e.getMessage(), map);
        } else {
            return new CustomError(element, e.getMessage());
        }
    }

    public void printStateMismatchError(CtElement element, String method, Predicate found, Predicate[] states) {
        List<RefinedVariable> lrv = new ArrayList<>(), mainVars = new ArrayList<>();
        gatherVariables(found, lrv, mainVars);
        TranslationTable map = new TranslationTable();
        VCImplication foundState = joinPredicates(found, mainVars, lrv, map);
        diagnostics.add(new StateRefinementError(element, method, states, foundState.toConjunctions(), map));
    }
}
