package liquidjava.processor.refinement_checker.object_checkers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import liquidjava.errors.ErrorHandler;
import liquidjava.processor.context.*;
import liquidjava.processor.refinement_checker.TypeChecker;
import liquidjava.processor.refinement_checker.TypeCheckingUtils;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.specification.StateRefinement;
import liquidjava.utils.Utils;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;

public class AuxStateHandler {

    // ########### Get State from StateRefinement declaration #############

    /**
     * Handles the passage of the written state annotations to the context for Constructors
     *
     * @param c
     * @param f
     * @param tc
     * 
     * @throws ParsingException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void handleConstructorState(CtConstructor<?> c, RefinedFunction f, TypeChecker tc)
            throws ParsingException {
        List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(c);
        if (!an.isEmpty()) {
            for (CtAnnotation<? extends Annotation> a : an) {
                Map<String, CtExpression> m = a.getAllValues();
                CtLiteral<String> from = (CtLiteral<String>) m.get("from");
                if (from != null) {
                    ErrorHandler.printErrorConstructorFromState(c, from, tc.getErrorEmitter());
                    return;
                }
            }
            setFunctionStates(f, an, tc, c);// f.setState(an, context.getGhosts(), c);
        } else {
            setDefaultState(f, tc);
        }

    }

    public static void setDefaultState(RefinedFunction f, TypeChecker tc) {
        String[] path = f.getTargetClass().split("\\.");
        String klass = path[path.length - 1];

        Predicate[] s = { Predicate.createVar(tc.THIS) };
        Predicate c = new Predicate();
        List<GhostFunction> sets = getDifferentSets(tc, klass);
        for (GhostFunction sg : sets) {
            if (sg.getReturnType().toString().equals("int")) {
                Predicate p = Predicate.createEquals(Predicate.createInvocation(sg.getName(), s),
                        Predicate.createLit("0", Utils.INT));
                c = Predicate.createConjunction(c, p);
            } else {
                fail("Ghost Functions not implemented for other types than int -> implement in AuxStateHandler defaultState");
            }
        }
        ObjectState os = new ObjectState();
        os.setTo(c);
        List<ObjectState> los = new ArrayList<>();
        los.add(os);
        f.setAllStates(los);
    }

    private static List<GhostFunction> getDifferentSets(TypeChecker tc, String klass) {
        List<GhostFunction> sets = new ArrayList<>();
        List<GhostState> l = tc.getContext().getGhostState(klass);
        if (l != null) {
            for (GhostState g : l) {
                if (g.getParent() == null) {
                    sets.add(g);
                } else if (!sets.contains(g.getParent())) {
                    sets.add(g.getParent());
                }

            }
        }
        return sets;
    }

    /**
     * Handles the passage of the written state annotations to the context for regular Methods
     *
     * @param method
     * @param f
     * @param tc
     * 
     * @throws ParsingException
     */
    public static void handleMethodState(CtMethod<?> method, RefinedFunction f, TypeChecker tc)
            throws ParsingException {
        List<CtAnnotation<? extends Annotation>> an = getStateAnnotation(method);
        if (!an.isEmpty()) {
            setFunctionStates(f, an, tc, method);
        }
        // f.setState(an, context.getGhosts(), method);

    }

    /**
     * Creates the list of states and adds them to the function
     *
     * @param f
     * @param anns
     * @param tc
     * @param element
     * 
     * @throws ParsingException
     */
    private static void setFunctionStates(RefinedFunction f, List<CtAnnotation<? extends Annotation>> anns,
            TypeChecker tc, CtElement element) throws ParsingException {
        List<ObjectState> l = new ArrayList<>();
        for (CtAnnotation<? extends Annotation> an : anns) {
            l.add(getStates(an, f, tc, element));
        }
        f.setAllStates(l);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static ObjectState getStates(CtAnnotation<? extends Annotation> ctAnnotation, RefinedFunction f,
            TypeChecker tc, CtElement e) throws ParsingException {
        Map<String, CtExpression> m = ctAnnotation.getAllValues();
        String from = TypeCheckingUtils.getStringFromAnnotation(m.get("from"));
        String to = TypeCheckingUtils.getStringFromAnnotation(m.get("to"));
        ObjectState state = new ObjectState();
        if (from != null) // has From
        {
            state.setFrom(createStatePredicate(from, f.getTargetClass(), tc, e, false));
        }
        if (to != null) // has To
        {
            state.setTo(createStatePredicate(to, f.getTargetClass(), tc, e, true));
        }

        if (from != null && to == null) // has From but not To -> the state remains the same
        {
            state.setTo(createStatePredicate(from, f.getTargetClass(), tc, e, true));
        }
        if (from == null && to != null) // has To but not From -> enters with true and exists with a specific state
        {
            state.setFrom(new Predicate());
        }
        return state;
    }

    private static Predicate createStatePredicate(String value, /* RefinedFunction f */ String targetClass,
            TypeChecker tc, CtElement e, boolean isTo) throws ParsingException {
        Predicate p = new Predicate(value, e, tc.getErrorEmitter());
        String t = targetClass; // f.getTargetClass();
        CtTypeReference<?> r = tc.getFactory().Type().createReference(t);

        String nameOld = String.format(tc.instanceFormat, tc.THIS, tc.getContext().getCounter());
        String name = String.format(tc.instanceFormat, tc.THIS, tc.getContext().getCounter());
        tc.getContext().addVarToContext(name, r, new Predicate(), e);
        tc.getContext().addVarToContext(nameOld, r, new Predicate(), e);
        // TODO REVIEW!!
        // what is it for?
        Predicate c1 = isTo ? getMissingStates(t, tc, p) : p;
        Predicate c = c1.substituteVariable(tc.THIS, name);
        c = c.changeOldMentions(nameOld, name, tc.getErrorEmitter());
        boolean b = tc.checksStateSMT(new Predicate(), c.negate(), e);
        if (b && !tc.getErrorEmitter().foundError()) {
            tc.createSameStateError(e, p, t);
        }

        return c1;

    }

    private static Predicate getMissingStates(String t, TypeChecker tc, Predicate p) {
        String[] temp = t.split("\\.");
        String simpleT = temp[temp.length - 1];
        List<GhostState> gs = p.getStateInvocations(tc.getContext().getGhostState(simpleT));
        List<GhostFunction> sets = getDifferentSets(tc, simpleT);
        for (GhostState g : gs) {
            if (g.getParent() == null && sets.contains(g)) {
                sets.remove(g);
            } else if (g.getParent() != null && sets.contains(g.getParent())) {
                sets.remove(g.getParent());
            }
        }
        return addOldStates(p, Predicate.createVar(tc.THIS), sets, tc);
    }

    /**
     * Create predicate with the equalities with previous versions of the object e.g., ghostfunction1(this) ==
     * ghostfunction1(old(this))
     *
     * @param p
     * @param th
     * @param sets
     * @param tc
     * 
     * @return
     */
    private static Predicate addOldStates(Predicate p, Predicate th, List<GhostFunction> sets, TypeChecker tc) {
        Predicate c = p;
        for (GhostFunction gf : sets) {
            Predicate eq = Predicate.createEquals( // gf.name == old(gf.name(this))
                    Predicate.createInvocation(gf.getName(), th),
                    Predicate.createInvocation(gf.getName(), Predicate.createInvocation(Utils.OLD, th)));
            c = Predicate.createConjunction(c, eq);
        }
        return c;
    }

    // ################ Handling State Change effects ################

    /**
     * Sets the new state acquired from the constructor call
     *
     * @param refKey
     * @param f
     * @param map
     * @param ctConstructorCall
     */
    public static void constructorStateMetadata(String refKey, RefinedFunction f, Map<String, String> map,
            CtConstructorCall<?> ctConstructorCall) {
        List<Predicate> oc = f.getToStates();
        if (oc.size() > 0) {// && !oc.get(0).isBooleanTrue())
            // ctConstructorCall.putMetadata(stateKey, oc.get(0));
            Predicate c = oc.get(0);
            for (String k : map.keySet()) {
                c = c.substituteVariable(k, map.get(k));
            }
            ctConstructorCall.putMetadata(refKey, c);
            // add maping to oc.get(0)-HERE
        } else if (oc.size() > 1) {
            assertFalse("Constructor can only have one to state, not multiple", true);
        }

    }

    /**
     * If an expression has a state in metadata, then its state is passed to the last instance of the variable with
     * varName
     *
     * @param tc
     * @param varName
     * @param e
     */
    public static void addStateRefinements(TypeChecker tc, String varName, CtExpression<?> e) {
        Optional<VariableInstance> ovi = tc.getContext().getLastVariableInstance(varName);
        if (ovi.isPresent() && e.getMetadata(tc.REFINE_KEY) != null) {
            VariableInstance vi = ovi.get();
            Predicate c = (Predicate) e.getMetadata(tc.REFINE_KEY);
            c = c.substituteVariable(tc.THIS, vi.getName()).substituteVariable(tc.WILD_VAR, vi.getName());
            vi.setRefinement(c);
        }
    }

    /**
     * Checks the changes in the state of the target
     *
     * @param tc
     * @param f
     * @param target2
     * @param target2
     * @param map
     * @param invocation
     */
    public static void checkTargetChanges(TypeChecker tc, RefinedFunction f, CtExpression<?> target2,
            Map<String, String> map, CtElement invocation) {
        String parentTargetName = searchFistVariableTarget(tc, target2, invocation);
        VariableInstance target = getTarget(tc, invocation);
        if (target != null) {
            if (f.hasStateChange() && f.getFromStates().size() > 0) {
                changeState(tc, target, f.getAllStates(), parentTargetName, map, invocation);
            }
            if (!f.hasStateChange()) {
                sameState(tc, target, parentTargetName, invocation);
            }
        }

    }

    public static void updateGhostField(CtFieldWrite<?> fw, TypeChecker tc) throws ParsingException {
        CtField<?> field = fw.getVariable().getDeclaration();
        String updatedVarName = String.format(tc.thisFormat, fw.getVariable().getSimpleName());
        String targetClass = field.getDeclaringType().getQualifiedName();

        // transition method construction
        CtMethod<?> transitionMethod = tc.getFactory().createMethod();
        transitionMethod.setType(tc.getFactory().createCtTypeReference(void.class));
        transitionMethod.setBody(tc.getFactory().createCtBlock(tc.getFactory().createCodeSnippetStatement()));
        transitionMethod.setSimpleName("_");

        transitionMethod.setParent(field.getDeclaringType());
        CtAnnotation<?> ann = tc.getFactory()
                .createAnnotation(tc.getFactory().createCtTypeReference(StateRefinement.class));

        // state transition annotation construction
        String stateChangeRefinementTo = field.getSimpleName() + "(this) == " + updatedVarName;
        String stateChangeRefinementFrom = "true";
        ann.addValue("to", stateChangeRefinementTo);
        ann.addValue("from", stateChangeRefinementFrom);

        transitionMethod.addAnnotation(ann);

        // extracting target from assignment
        System.out.println("Target for invocation: " + fw.getTarget());
        CtInvocation<?> inv = tc.getFactory().createInvocation(fw.getTarget(), transitionMethod.getReference(),
                Collections.emptyList());

        // applying transition:
        // StateRefinement(from="true", to="n(this)=this#n")
        // ObjectState stateChange = getStates(ann, rf, tc, transitionMethod);

        ObjectState stateChange = new ObjectState();
        Predicate fromPredicate = createStatePredicate(stateChangeRefinementFrom, targetClass, tc, transitionMethod,
                false);
        Predicate toPredicate = createStatePredicate(stateChangeRefinementTo, targetClass, tc, transitionMethod, true);
        stateChange.setFrom(fromPredicate);
        stateChange.setTo(toPredicate);

        // AuxStateHandler.checkTargetChanges(tc, rf, inv.getTarget(), Collections.emptyMap(), inv);
        String parentTargetName = searchFistVariableTarget(tc, inv.getTarget(), inv);
        // ^- metadata is put here
        // v- and accessed here
        VariableInstance vi = getTarget(tc, inv);

        if (vi == null) {
            return;
        }
        // changeState(tc, vi, Collections.singletonList(stateChange), parentTargetName, Collections.emptyMap(), inv);
        String instanceName = vi.getName();
        Predicate prevState = vi.getRefinement().substituteVariable(tc.WILD_VAR, instanceName)
                .substituteVariable(parentTargetName, instanceName);

        // replace "state(this)" to "state(whatever method is called from) and so on"
        Predicate expectState = stateChange.getFrom().substituteVariable(tc.THIS, instanceName);
        expectState = expectState.changeOldMentions(vi.getName(), instanceName, tc.getErrorEmitter());

        boolean found = tc.checksStateSMT(prevState, expectState, inv);
        if (found) {
            String newInstanceName = String.format(tc.instanceFormat, parentTargetName, tc.getContext().getCounter());
            Predicate transitionedState = stateChange.getTo().substituteVariable(tc.WILD_VAR, newInstanceName)
                    .substituteVariable(tc.THIS, newInstanceName);

            transitionedState = checkOldMentions(transitionedState, instanceName, newInstanceName, tc);
            // update of stata of new instance of this#n#(whatever it was + 1)
            addInstanceWithState(tc, parentTargetName, newInstanceName, vi, transitionedState, inv);
        }

        if (!found && !tc.getErrorEmitter().foundError()) {// Reaches the end of stateChange no matching states
            String states = stateChange.getFrom().toString();
            String simpleInvocation = inv.toString();
            tc.createStateMismatchError(inv, simpleInvocation, prevState, states);
        }

    }

    /**
     * Changes the state
     *
     * @param tc
     * @param vi
     * @param stateChanges
     * @param name
     * @param map
     * @param invocation
     * 
     * @return
     */
    private static Predicate changeState(TypeChecker tc, VariableInstance vi,
            /* RefinedFunction f */ List<ObjectState> stateChanges, String name, Map<String, String> map,
            CtElement invocation) {
        if (vi.getRefinement() == null) {
            return new Predicate();
        }
        String instanceName = vi.getName();
        Predicate prevState = vi.getRefinement().substituteVariable(tc.WILD_VAR, instanceName).substituteVariable(name,
                instanceName);

        // List<ObjectState> stateChanges = f.getAllStates();

        boolean found = false;
        // if(los.size() > 1)
        // assertFalse("Change state only working for one method with one state",true);
        for (ObjectState stateChange : stateChanges) {// TODO: only working for 1 state annotation
            if (found) {
                break;
            }
            if (!stateChange.hasFrom()) {
                continue;
            }
            // replace "state(this)" to "state(whatever method is called from) and so on"
            Predicate expectState = stateChange.getFrom().substituteVariable(tc.THIS, instanceName);
            Predicate prevCheck = prevState;
            for (String s : map.keySet()) {// substituting function variables into annotation if there are any
                prevCheck = prevCheck.substituteVariable(s, map.get(s));
                expectState = expectState.substituteVariable(s, map.get(s));
            }
            expectState = expectState.changeOldMentions(vi.getName(), instanceName, tc.getErrorEmitter());

            found = tc.checksStateSMT(prevCheck, expectState, invocation);
            if (found && stateChange.hasTo()) {
                String newInstanceName = String.format(tc.instanceFormat, name, tc.getContext().getCounter());
                Predicate transitionedState = stateChange.getTo().substituteVariable(tc.WILD_VAR, newInstanceName)
                        .substituteVariable(tc.THIS, newInstanceName);
                for (String s : map.keySet()) {
                    transitionedState = transitionedState.substituteVariable(s, map.get(s));
                }
                transitionedState = checkOldMentions(transitionedState, instanceName, newInstanceName, tc);
                // update of stata of new instance of this#n#(whatever it was + 1)
                addInstanceWithState(tc, name, newInstanceName, vi, transitionedState, invocation);
                return transitionedState;

            }
        }
        if (!found && !tc.getErrorEmitter().foundError()) {// Reaches the end of stateChange no matching states
            String states = stateChanges.stream().filter(ObjectState::hasFrom).map(ObjectState::getFrom)
                    .map(Predicate::toString).collect(Collectors.joining(","));

            String simpleInvocation = invocation.toString(); // .getExecutable().toString();
            tc.createStateMismatchError(invocation, simpleInvocation, prevState, states);
            // ErrorPrinter.printStateMismatch(invocation, simpleInvocation, prevState, states);
        }
        return new Predicate();
    }

    private static Predicate checkOldMentions(Predicate transitionedState, String instanceName, String newInstanceName,
            TypeChecker tc) {
        return transitionedState.changeOldMentions(instanceName, newInstanceName, tc.getErrorEmitter());
    }

    /**
     * Copies the previous state to the new variable instance
     *
     * @param tc
     * @param variableInstance
     * @param name
     * @param invocation
     * 
     * @return
     */
    private static Predicate sameState(TypeChecker tc, VariableInstance variableInstance, String name,
            CtElement invocation) {
        // if(variableInstance.getState() != null) {
        if (variableInstance.getRefinement() != null) {
            String newInstanceName = String.format(tc.instanceFormat, name, tc.getContext().getCounter());
            // Predicate c = variableInstance.getState().substituteVariable(variableInstance.getName(),
            // newInstanceName);
            Predicate c = variableInstance.getRefinement().substituteVariable(tc.WILD_VAR, newInstanceName)
                    .substituteVariable(variableInstance.getName(), newInstanceName);

            addInstanceWithState(tc, name, newInstanceName, variableInstance, c, invocation);
            return c;
        }
        return new Predicate();
    }

    /**
     * Adds a new instance with the given state to the parent variable
     *
     * @param tc
     * @param superName
     * @param name2
     * @param prevInstance
     * @param transitionedState
     * @param invocation
     * 
     * @return
     */
    private static String addInstanceWithState(TypeChecker tc, String superName, String name2,
            VariableInstance prevInstance, Predicate transitionedState, CtElement invocation) {
        VariableInstance vi2 = (VariableInstance) tc.getContext().addInstanceToContext(name2, prevInstance.getType(),
                prevInstance.getRefinement(), invocation);
        // vi2.setState(transitionedState);
        vi2.setRefinement(transitionedState);
        RefinedVariable rv = tc.getContext().getVariableByName(superName);
        for (CtTypeReference<?> t : rv.getSuperTypes()) {
            vi2.addSuperType(t);
        }

        // if the variable is a parent (not a VariableInstance) we need to check that this refinement
        // is a subtype of the variable's main refinement
        if (rv instanceof Variable) {
            Predicate superC = rv.getMainRefinement().substituteVariable(rv.getName(), vi2.getName());
            tc.checkSMT(superC, invocation);
            tc.getContext().addRefinementInstanceToVariable(superName, name2);
        }

        invocation.putMetadata(tc.TARGET_KEY, vi2);
        return name2;
    }

    /**
     * Gets the name of the parent target and adds the closest target to the elem TARGET metadata
     *
     * @param invocation
     * 
     * @return
     */
    static String searchFistVariableTarget(TypeChecker tc, CtElement target2, CtElement invocation) {
        if (target2 instanceof CtVariableRead<?>) {
            // v--------- field read
            // means invokation is in a form of `t.method(args)`
            CtVariableRead<?> v = (CtVariableRead<?>) target2;
            String name = v.getVariable().getSimpleName();
            Optional<VariableInstance> invocation_callee = tc.getContext().getLastVariableInstance(name);
            if (invocation_callee.isPresent()) {
                invocation.putMetadata(tc.TARGET_KEY, invocation_callee.get());
            } else if (target2.getMetadata(tc.TARGET_KEY) == null) {
                RefinedVariable var = tc.getContext().getVariableByName(name);
                String nName = String.format(tc.instanceFormat, name, tc.getContext().getCounter());
                RefinedVariable rv = tc.getContext().addInstanceToContext(nName, var.getType(),
                        var.getRefinement().substituteVariable(name, nName), target2);
                tc.getContext().addRefinementInstanceToVariable(name, nName);
                invocation.putMetadata(tc.TARGET_KEY, rv);
            }

            return name;
        } else if (target2.getMetadata(tc.TARGET_KEY) != null) {
            // invokation is in
            // who did put the metadata here then?
            VariableInstance target2_vi = (VariableInstance) target2.getMetadata(tc.TARGET_KEY);
            Optional<Variable> v = target2_vi.getParent();
            invocation.putMetadata(tc.TARGET_KEY, target2_vi);
            return v.map(Refined::getName).orElse(target2_vi.getName());
        }
        return null;
    }

    static VariableInstance getTarget(TypeChecker tc, CtElement invocation) {
        if (invocation.getMetadata(tc.TARGET_KEY) != null) {
            return (VariableInstance) invocation.getMetadata(tc.TARGET_KEY);
        }
        return null;
    }

    private static List<CtAnnotation<? extends Annotation>> getStateAnnotation(CtElement element) {
        return element.getAnnotations().stream().filter(ann -> ann.getActualAnnotation().annotationType()
                .getCanonicalName().contentEquals("liquidjava.specification.StateRefinement"))
                .collect(Collectors.toList());

        // List<CtAnnotation<? extends Annotation>> l = new ArrayList<CtAnnotation<? extends Annotation>>();
        // for (CtAnnotation<? extends Annotation> ann : element.getAnnotations()) {
        // String an = ann.getActualAnnotation().annotationType().getCanonicalName();
        // if (an.contentEquals("liquidjava.specification.StateRefinement")) {
        // l.add(ann);
        // }
        // }
        // return l;
    }

}
