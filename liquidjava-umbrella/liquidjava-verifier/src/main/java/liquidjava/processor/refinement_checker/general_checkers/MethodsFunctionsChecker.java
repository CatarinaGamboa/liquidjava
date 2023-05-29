package liquidjava.processor.refinement_checker.general_checkers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import liquidjava.processor.context.*;
import liquidjava.processor.heap.HeapContext;
import liquidjava.processor.refinement_checker.TypeChecker;
import liquidjava.processor.refinement_checker.VCChecker;
import liquidjava.processor.refinement_checker.object_checkers.AuxHierarchyRefinememtsPassage;
import liquidjava.processor.refinement_checker.object_checkers.AuxStateHandler;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.SepUnit;
import liquidjava.rj_language.parsing.ParsingException;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtExecutableReference;

public class MethodsFunctionsChecker {

    private final TypeChecker rtc;

    private static final String retNameFormat = "#ret_%d";

    public MethodsFunctionsChecker(TypeChecker rtc) {
        this.rtc = rtc;

    }

    public void getConstructorRefinements(CtConstructor<?> c) throws ParsingException {
        RefinedFunction f = new RefinedFunction();
        f.setName(c.getSimpleName());
        f.setType(c.getType());
        handleFunctionRefinements(f, c, c.getParameters());
        f.setRefReturn(new Predicate());// why it is set to true, despite being handle in `handleFunctionRefinement`?

        if (f.getHeapChange().isId()) {
            f.setHeapChange(HeapContext.Transition.simpleConstructorTransition());
        }
        // sep logic: Maybe I should do everything in handleFunctionRefinement, not here
        if (c.getParent() instanceof CtClass) {
            CtClass<?> klass = (CtClass<?>) c.getParent();
            f.setClass(klass.getQualifiedName());
        }
        rtc.getContext().addFunctionToContext(f);
        AuxStateHandler.handleConstructorState(c, f, rtc);
    }

    public void getConstructorInvocationRefinements(CtConstructorCall<?> ctConstructorCall) {
        CtExecutableReference<?> exe = ctConstructorCall.getExecutable();
        if (exe == null) {
            return;
        }
        RefinedFunction f = rtc.getContext().getFunction(exe.getSimpleName(), exe.getDeclaringType().getQualifiedName(),
                ctConstructorCall.getArguments().size());
        if (f == null) {
            return;
        }
        Map<String, String> map = checkInvocationRefinements(ctConstructorCall, ctConstructorCall.getArguments(),
                ctConstructorCall.getTarget(), f.getName(), f.getTargetClass());
        AuxStateHandler.constructorStateMetadata(rtc.REFINE_KEY, f, map, ctConstructorCall);

        // TODO(sep logic): here should be `new` handling, probably
        // but its actually handled in the same place where constructor refinements are handled
        //

    }

    // ################### VISIT METHOD ##############################
    public <R> void getMethodRefinements(CtMethod<R> method) throws ParsingException {
        RefinedFunction f = new RefinedFunction();
        f.setName(method.getSimpleName());
        f.setType(method.getType());
        f.setRefReturn(new Predicate());

        CtClass<?> klass = null;
        if (method.getParent() instanceof CtClass) {
            klass = (CtClass<?>) method.getParent();
            f.setClass(klass.getQualifiedName());
        }
        if (method.getParent() instanceof CtInterface<?>) {
            CtInterface<?> inter = (CtInterface<?>) method.getParent();
            f.setClass(inter.getQualifiedName());
        }
        rtc.getContext().addFunctionToContext(f);

        auxGetMethodRefinements(method, f);
        // TODO(sep logic): handle heap change
        // But here implicitly handleFunctionRefinement is called,
        // so maybe nothing should be done here
        AuxStateHandler.handleMethodState(method, f, rtc);

        if (klass != null)
            AuxHierarchyRefinememtsPassage.checkFunctionInSupertypes(klass, method, f, rtc);

    }

    public <R> void getMethodRefinements(CtMethod<R> method, String prefix) throws ParsingException {
        String constructorName = "<init>";
        String[] pac = prefix.split("\\.");
        String k = pac[pac.length - 1];

        String functionName = String.format("%s.%s", prefix, method.getSimpleName());
        if (k.equals(method.getSimpleName())) {// is a constructor
            functionName = String.format(constructorName);
        }

        RefinedFunction f = new RefinedFunction();
        f.setName(functionName);
        f.setType(method.getType());
        f.setRefReturn(new Predicate());

        f.setClass(prefix);
        rtc.getContext().addFunctionToContext(f);
        auxGetMethodRefinements(method, f);
        // TODO(sep logic): handle heap change
        // internally handleFunctionRefinement is called
        // maybe nothing to do here

        AuxStateHandler.handleMethodState(method, f, rtc);
        if (functionName.equals(constructorName) && !f.hasStateChange()) {
            AuxStateHandler.setDefaultState(f, rtc);
        }
    }

    private <R> void auxGetMethodRefinements(CtMethod<R> method, RefinedFunction rf) throws ParsingException {
        // main cannot have refinement - for now
        if (method.getSignature().equals("main(java.lang.String[])"))
            return;
        List<CtParameter<?>> params = method.getParameters();
        Predicate ref = handleFunctionRefinements(rf, method, params);
        method.putMetadata(rtc.REFINE_KEY, ref);

    }

    /**
     * Joins all the refinements from parameters and return
     *
     * @param f
     *            internal representation of function
     * @param method
     *            Spoon's representation of function
     * @param params
     *            Spoon's representation of function arguments
     *
     * @return Conjunction of predicates in method and its arguments
     *
     * @throws ParsingException
     *             when annotation parsing is failed
     */
    private Predicate handleFunctionRefinements(RefinedFunction f, CtElement method, List<CtParameter<?>> params)
            throws ParsingException {
        Predicate joint = new Predicate();

        for (CtParameter<?> param : params) {
            String paramName = param.getSimpleName();
            Predicate c = rtc.getRefinementFromAnnotation(param).orElse(Predicate.booleanTrue())
                    .makeSubstitution(rtc.WILD_VAR, paramName);
            param.putMetadata(rtc.REFINE_KEY, c);
            RefinedVariable v = rtc.getContext().addVarToContext(param.getSimpleName(), param.getType(), c, param);
            if (v instanceof Variable)
                f.addArgRefinements((Variable) v);
            joint = Predicate.createConjunction(joint, c);
        }

        Predicate ret = rtc.getRefinementFromAnnotation(method).orElse(Predicate.booleanTrue());
        f.setRefReturn(ret);

        HeapContext.Transition tr = rtc.getHeapRefinementFromAnnotation(method).orElse(HeapContext.Transition.id());
        f.setHeapChange(tr);

        // TODO(sep logic): handle heap change
        // I think maybe just store heap transition with substituted variables
        // in rtc.HEAP_TR_KEY

        // (z == y & x -> ? * z -> ?) =>/-* (x -> ? * y -> ?)

        // A & (A -* B)
        // ----------------
        // B

        // -> = not (_ && not _)
        // forall free_vars. (premise => conclusion) -> not (exists free_vars . not (premise => conclusion))

        // assert(z == y & x -> ? * z -> ?)
        // assert(not ((z == y & x -> ? * y -> ?)))

        // rtc.context.addFunctionToContext(f);
        return Predicate.createConjunction(joint, ret);
    }

    public List<CtAnnotation<? extends Annotation>> getStateAnnotation(CtElement element) {
        List<CtAnnotation<? extends Annotation>> l = new ArrayList<>();
        for (CtAnnotation<? extends Annotation> ann : element.getAnnotations()) {
            String an = ann.getActualAnnotation().annotationType().getCanonicalName();
            if (an.contentEquals("liquidjava.specification.StateRefinement")) {
                l.add(ann);
            }
        }
        return l;
    }

    public <R> void getReturnRefinements(CtReturn<R> ret) {
        CtClass<?> c = ret.getParent(CtClass.class);
        String className = c.getSimpleName();
        if (ret.getReturnedExpression() == null) {
            return;
        }
        // check if there are refinements
        if (rtc.getRefinement(ret.getReturnedExpression()) == null) {
            ret.getReturnedExpression().putMetadata(rtc.REFINE_KEY, Predicate.booleanTrue());
        }
        CtMethod<?> method = ret.getParent(CtMethod.class);
        // check if method has refinements
        if (rtc.getRefinement(method) == null) {
            return;
        }
        if (method.getParent() instanceof CtClass) {
            RefinedFunction fi = rtc.getContext().getFunction(method.getSimpleName(),
                    ((CtClass<?>) method.getParent()).getQualifiedName());

            fi.getArguments().forEach(v -> rtc.getContext().addVarToContext(v));

            // Both return and the method have metadata
            String thisName = String.format(rtc.thisFormat, className);
            rtc.getContext().addInstanceToContext(thisName, c.getReference(), Predicate.booleanTrue(), ret);
            // ???
            String returnVarName = String.format(retNameFormat, rtc.getContext().getCounter());
            Predicate cretRef = rtc.getRefinement(ret.getReturnedExpression())
                    .makeSubstitution(rtc.WILD_VAR, returnVarName).makeSubstitution(rtc.THIS, returnVarName);
            Predicate cexpectedType = fi.getRefReturn().makeSubstitution(rtc.WILD_VAR, returnVarName)
                    .makeSubstitution(rtc.THIS, returnVarName);

            rtc.getContext().addVarToContext(returnVarName, method.getType(), cretRef, ret);
            rtc.checkSMT(cexpectedType, ret);
            rtc.getContext().newRefinementToVariableInContext(returnVarName, cexpectedType);

            // TODO(sep logic): verify postcondition
        }

    }

    // ############################### VISIT INVOCATION ################################3

    public <R> void getInvocationRefinements(CtInvocation<R> invocation) {
        CtExecutable<?> method = invocation.getExecutable().getDeclaration();
        if (method == null) {
            Method m = invocation.getExecutable().getActualMethod();
            if (m != null)
                searchMethodInLibrary(m, invocation);// calls checkInvocationRefinements

        } else if (method.getParent() instanceof CtClass) {
            String ctype = ((CtClass<?>) method.getParent()).getQualifiedName();
            RefinedFunction f = rtc.getContext().getFunction(method.getSimpleName(), ctype);
            if (f != null) {// inside rtc.context
                checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(),
                        method.getSimpleName(), ctype);

            } else {
                CtExecutable<?> cet = invocation.getExecutable().getDeclaration();
                if (cet instanceof CtMethod) {
                    // CtMethod met = (CtMethod) cet;
                    // rtc.visitCtMethod(met);
                    // checkInvocationRefinements(invocation, method.getSimpleName());
                }
                // rtc.visitCtMethod(method);

            }
        }
    }

    public RefinedFunction getRefinementFunction(String methodName, String className, int size) {
        RefinedFunction f = rtc.getContext().getFunction(methodName, className, size);
        if (f == null) {
            f = rtc.getContext().getFunction(String.format("%s.%s", className, methodName), methodName, size);
        }
        return f;
    }

    private void searchMethodInLibrary(Method m, CtInvocation<?> invocation) {
        System.out.println("Searching method in library: " + m);
        String ctype = m.getDeclaringClass().getCanonicalName();
        if (rtc.getContext().getFunction(m.getName(), ctype) != null) {// inside rtc.context
            checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(), m.getName(),
                    ctype);
            return;
        } else {
            String name = m.getName();
            String prefix = m.getDeclaringClass().getCanonicalName();
            String completeName = String.format("%s.%s", prefix, name);
            if (rtc.getContext().getFunction(completeName, ctype) != null) {
                checkInvocationRefinements(invocation, invocation.getArguments(), invocation.getTarget(), completeName,
                        ctype);
            }

        }

    }

    private Map<String, String> checkInvocationRefinements(CtElement invocation, List<CtExpression<?>> arguments,
            CtExpression<?> target, String methodName, String className) {
        // TODO(sep logic): check if heap is ok and apply changes
        // Here I should actually call frame rule. Maybe it is the only place to do so

        // -- Part 1: Check if the invocation is possible
        int si = arguments.size();
        RefinedFunction f = rtc.getContext().getFunction(methodName, className, si);
        Map<String, String> map = mapInvocation(arguments, f);
        HeapContext.Transition heapTransition = f.getHeapChange().clone();

        if (target != null) {
            AuxStateHandler.checkTargetChanges(rtc, f, target, map, invocation);
        }
        if (f.allRefinementsTrue() && heapTransition.isId()) {
            invocation.putMetadata(rtc.REFINE_KEY, Predicate.booleanTrue());
            return map;
        }

        checkParameters(invocation, arguments, f, map); // <- only actual check via SMT solver, besides heap change

        // -- Part 2: Apply changes
        // applyRefinementsToArguments(element, arguments, f, map);
        final Predicate methodRef = f.getRefReturn().clone();

        if (methodRef == null) {
            return map;
        }

        boolean equalsThis = methodRef.toString().equals("(_ == this)"); // TODO change for better
        List<String> vars = methodRef.getVariableNames();
        vars.stream().filter(map::containsKey).forEach(s -> methodRef.substituteInPlace(s, map.get(s)));

        String varName = null;
        if (invocation.getMetadata(rtc.TARGET_KEY) != null) {
            VariableInstance vi = (VariableInstance) invocation.getMetadata(rtc.TARGET_KEY);
            methodRef.substituteInPlace(rtc.THIS, vi.getName());
            Variable v = rtc.getContext().getVariableFromInstance(vi);
            if (v != null)
                varName = v.getName();
        }

        String viName = String.format(rtc.instanceFormat, f.getName(), rtc.getContext().getCounter());
        VariableInstance vi = (VariableInstance) rtc.getContext().addInstanceToContext(viName, f.getType(),
                methodRef.makeSubstitution(rtc.WILD_VAR, viName), invocation); // TODO REVER!!
        if (varName != null && f.hasStateChange() && equalsThis)
            rtc.getContext().addRefinementInstanceToVariable(varName, viName);
        invocation.putMetadata(rtc.TARGET_KEY, vi);
        invocation.putMetadata(rtc.REFINE_KEY, methodRef);
        // TODO(sep logic): invocation.puMetadata(rtc.HEAP_KEY, newHeapContext) ????
        // YES it is likely a right thing to do.
        // Is there a place to reuse the stored value?

        // heap change: there are approx len(HeapCtx) + 1 checks via smt-solver
        rtc.changeHeap(methodRef, heapTransition.substituteWildVar(viName).substituteFromMap(map), invocation);

        return map;

    }

    private <R> Map<String, String> mapInvocation(List<CtExpression<?>> invocationParams, RefinedFunction f) {
        Map<String, String> mapInvocation = new HashMap<>();
        List<Variable> functionParams = f.getArguments();
        for (int i = 0; i < invocationParams.size(); i++) {
            Variable fArg = functionParams.get(i);
            CtExpression<?> iArg = invocationParams.get(i);
            String invStr;
            // if(iArg instanceof CtLiteral)
            // invStr = iArg.toString();
            // else
            if (iArg instanceof CtFieldRead) {
                invStr = createVariableRepresentingArgument(iArg, fArg);
            } else if (iArg instanceof CtVariableRead) {
                CtVariableRead<?> vr = (CtVariableRead<?>) iArg;
                Optional<VariableInstance> ovi = rtc.getContext()
                        .getLastVariableInstance(vr.getVariable().getSimpleName());
                invStr = ovi.map(Refined::getName).orElse(vr.toString());
            } else // create new variable with the argument refinement
                invStr = createVariableRepresentingArgument(iArg, fArg);

            mapInvocation.put(fArg.getName(), invStr);
        }
        return mapInvocation;
    }

    private String createVariableRepresentingArgument(CtExpression<?> iArg, Variable fArg) {
        Predicate met = (Predicate) iArg.getMetadata(rtc.REFINE_KEY);
        if (met == null)
            met = new Predicate();
        if (!met.getVariableNames().contains(rtc.WILD_VAR))
            met = Predicate.createEquals(Predicate.createVar(rtc.WILD_VAR), met);
        String nVar = String.format(rtc.instanceFormat, fArg.getName(), rtc.getContext().getCounter());
        rtc.getContext().addInstanceToContext(nVar, fArg.getType(), met.makeSubstitution(rtc.WILD_VAR, nVar), iArg);
        return nVar;
    }

    private <R> void checkParameters(CtElement invocation, List<CtExpression<?>> invocationParams, RefinedFunction f,
            Map<String, String> map) {
        List<Variable> functionParams = f.getArguments();
        functionParams.forEach(fArg -> {
            Predicate c = fArg.getMainRefinement().clone();
            c.substituteInPlace(fArg.getName(), map.get(fArg.getName()));
            List<String> vars = c.getVariableNames();
            vars.stream().filter(map::containsKey).forEach(s -> c.substituteInPlace(s, map.get(s)));
            rtc.checkSMT(c, invocation);
        });

    }

    // IN CONSTRUCTION _ NOT USED
    @SuppressWarnings("unused")
    private void applyRefinementsToArguments(CtElement element, List<CtExpression<?>> invocationParams,
            RefinedFunction f, Map<String, String> map) {
        Context context = rtc.getContext();
        List<Variable> functionParams = f.getArguments();

        for (int i = 0; i < invocationParams.size(); i++) {
            Variable fArg = functionParams.get(i);
            Predicate inferredRefinement = fArg.getRefinement();

            CtExpression<?> e = invocationParams.get(i);
            if (e instanceof CtVariableRead<?>) {
                CtVariableRead<?> v = (CtVariableRead<?>) e;
                String varName = v.getVariable().getSimpleName(); // TODO CHANGE
                RefinedVariable rv = context.getVariableByName(varName);
                String instanceName = String.format(rtc.instanceFormat, varName, context.getCounter());

                inferredRefinement = inferredRefinement.makeSubstitution(fArg.getName(), instanceName);
                context.addInstanceToContext(instanceName, rv.getType(), inferredRefinement, element);
                context.addRefinementInstanceToVariable(varName, instanceName);

            } // TODO else's?

            // c = c.substituteVariable(fArg.getName(), map.get(fArg.getName()));
            // List<String> vars = c.getVariableNames();
            // for(String s: vars)
            // if(map.containsKey(s))
            // c = c.substituteVariable(s, map.get(s));
            // rtc.checkSMT(c, invocation);
        }

    }

    public void loadFunctionInfo(CtExecutable<?> method) {
        String className = null;
        if (method.getParent() instanceof CtClass) {
            className = ((CtClass<?>) method.getParent()).getQualifiedName();
        } else if (method.getParent() instanceof CtInterface<?>) {
            className = ((CtInterface<?>) method.getParent()).getQualifiedName();
        }
        if (className != null) {
            RefinedFunction fi = getRefinementFunction(method.getSimpleName(), className,
                    method.getParameters().size());
            if (fi != null) {
                List<Variable> lv = fi.getArguments();
                for (Variable v : lv)
                    rtc.getContext().addVarToContext(v);
            } else {
                fail("Method should already be in context. Should not arrive this point in refinement checker.");
                // getMethodRefinements(method); //should be irrelevant -should never need to get here
            }
        }

    }

}
