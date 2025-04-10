package liquidjava.processor.refinement_checker.object_checkers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import liquidjava.processor.context.ObjectState;
import liquidjava.processor.context.RefinedFunction;
import liquidjava.processor.context.RefinedVariable;
import liquidjava.processor.context.Variable;
import liquidjava.processor.refinement_checker.TypeChecker;
import liquidjava.rj_language.Predicate;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtTypeReference;

public class AuxHierarchyRefinememtsPassage {

    public static <R> void checkFunctionInSupertypes(
            CtClass<?> klass, CtMethod<R> method, RefinedFunction f, TypeChecker tc) {
        String name = method.getSimpleName();
        int size = method.getParameters().size();
        if (klass.getSuperInterfaces().size() > 0) { // implemented interfaces
            Optional<RefinedFunction> superFunction = functionInInterface(klass, name, size, tc);
            if (superFunction.isPresent()) {
                transferRefinements(superFunction.get(), f, method, tc);
            }
        }
        if (klass.getSuperclass() != null) { // extended class
            CtTypeReference<?> t = klass.getSuperclass();
            RefinedFunction superFunction = tc.getContext().getFunction(name, t.getQualifiedName(), size);
            if (superFunction != null) {
                transferRefinements(superFunction, f, method, tc);
            }
        }
    }

    static void transferRefinements(
            RefinedFunction superFunction, RefinedFunction function, CtMethod<?> method, TypeChecker tc) {
        HashMap<String, String> super2function = getParametersMap(superFunction, function, tc, method);
        transferReturnRefinement(superFunction, function, method, tc, super2function);
        transferArgumentsRefinements(superFunction, function, method, tc, super2function);
        transferStateRefinements(superFunction, function, method, tc);
    }

    private static HashMap<String, String> getParametersMap(
            RefinedFunction superFunction, RefinedFunction function, TypeChecker tc, CtMethod<?> method) {
        List<Variable> superArgs = superFunction.getArguments();
        List<Variable> fArgs = function.getArguments();
        HashMap<String, String> m = new HashMap<String, String>();
        for (int i = 0; i < fArgs.size(); i++) {
            String newName = String.format(
                    tc.instanceFormat, fArgs.get(i).getName(), tc.getContext().getCounter());
            m.put(superArgs.get(i).getName(), newName);
            m.put(fArgs.get(i).getName(), newName);
            RefinedVariable rv = tc.getContext()
                    .addVarToContext(
                            newName,
                            superArgs.get(i).getType(),
                            new Predicate(),
                            method.getParameters().get(i));
            for (CtTypeReference<?> t : fArgs.get(i).getSuperTypes()) {
                rv.addSuperType(t);
            }
        }
        return m;
    }

    static void transferArgumentsRefinements(
            RefinedFunction superFunction,
            RefinedFunction function,
            CtMethod<?> method,
            TypeChecker tc,
            HashMap<String, String> super2function) {
        List<Variable> superArgs = superFunction.getArguments();
        List<Variable> args = function.getArguments();
        List<CtParameter<?>> params = method.getParameters();
        for (int i = 0; i < args.size(); i++) {
            Variable arg = args.get(i);
            Variable superArg = superArgs.get(i);
            String newName = super2function.get(arg.getName());
            // create new name
            Predicate argRef = arg.getRefinement().substituteVariable(arg.getName(), newName);
            Predicate superArgRef = superArg.getRefinement().substituteVariable(superArg.getName(), newName);

            if (argRef.isBooleanTrue()) {
                arg.setRefinement(superArgRef.substituteVariable(newName, arg.getName()));
            } else {
                boolean f = tc.checksStateSMT(superArgRef, argRef, params.get(i).getPosition());
                if (!f) {
                    // ErrorPrinter.printError(method, argRef, superArgRef);
                    if (!tc.getErrorEmitter().foundError()) tc.createError(method, argRef, superArgRef, "");
                }
            }
        }
    }

    static void transferReturnRefinement(
            RefinedFunction superFunction,
            RefinedFunction function,
            CtMethod<?> method,
            TypeChecker tc,
            HashMap<String, String> super2function) {
        Predicate functionRef = function.getRefinement();
        Predicate superRef = superFunction.getRefinement();
        if (functionRef.isBooleanTrue()) function.setRefinement(superRef);
        else {
            String name = String.format(tc.freshFormat, tc.getContext().getCounter());
            tc.getContext().addVarToContext(name, superFunction.getType(), new Predicate(), method);
            // functionRef might be stronger than superRef -> check (superRef <:
            // functionRef)
            functionRef = functionRef.substituteVariable(tc.WILD_VAR, name);
            superRef = superRef.substituteVariable(tc.WILD_VAR, name);
            for (String m : super2function.keySet()) superRef = superRef.substituteVariable(m, super2function.get(m));
            for (String m : super2function.keySet())
                functionRef = functionRef.substituteVariable(m, super2function.get(m));

            tc.checkStateSMT(
                    functionRef,
                    superRef,
                    method,
                    "Return Refinement of Subclass must be subtype of the Return Refinement of the" + " Superclass");
            // boolean f = tc.checkStateSMT(functionRef, superRef, method);
            // if(!f)
            // ErrorPrinter.printError(method, superRef, functionRef);
        }
    }

    static Optional<RefinedFunction> functionInInterface(
            CtClass<?> klass, String simpleName, int size, TypeChecker tc) {
        List<RefinedFunction> lrf = tc.getContext().getAllMethodsWithNameSize(simpleName, size);
        List<String> st = klass.getSuperInterfaces().stream()
                .map(p -> p.getQualifiedName())
                .collect(Collectors.toList());
        for (RefinedFunction rf : lrf) {
            if (st.contains(rf.getTargetClass())) return Optional.of(rf); // TODO only works for 1 interface
        }
        return Optional.empty();
    }

    private static void transferStateRefinements(
            RefinedFunction superFunction, RefinedFunction subFunction, CtMethod<?> method, TypeChecker tc) {
        if (superFunction.hasStateChange()) {
            if (!subFunction.hasStateChange()) {
                for (ObjectState o : superFunction.getAllStates()) subFunction.addStates(o.clone());
            } else {
                List<ObjectState> superStates = superFunction.getAllStates();
                List<ObjectState> subStates = subFunction.getAllStates();
                for (int i = 0; i < superStates.size(); i++) {
                    ObjectState superState = superStates.get(i);
                    ObjectState subState = subStates.get(i);

                    String thisName =
                            String.format(tc.freshFormat, tc.getContext().getCounter());
                    createVariableInContext(
                            thisName,
                            tc,
                            subFunction,
                            superFunction,
                            method.getParameters().get(i));

                    Predicate superConst = matchVariableNames(tc.THIS, thisName, superState.getFrom());
                    Predicate subConst =
                            matchVariableNames(tc.THIS, thisName, superFunction, subFunction, subState.getFrom());

                    // fromSup <: fromSub <==> fromSup is sub type and fromSub is expectedType
                    tc.checkStateSMT(
                            superConst,
                            subConst,
                            method,
                            "FROM State from Superclass must be subtype of FROM State from Subclass");
                    // boolean correct = tc.checkStateSMT(superConst, subConst, method);
                    // if(!correct) ErrorPrinter.printError(method, subState.getFrom(),
                    // superState.getFrom());

                    superConst = matchVariableNames(tc.THIS, thisName, superState.getTo());
                    subConst = matchVariableNames(tc.THIS, thisName, superFunction, subFunction, subState.getTo());
                    // toSub <: toSup <==> ToSub is sub type and toSup is expectedType
                    tc.checkStateSMT(
                            subConst,
                            superConst,
                            method,
                            "TO State from Subclass must be subtype of TO State from Superclass");
                    // boolean correct = tc.checkStateSMT(subConst, superConst, method);
                    // if(!correct) ErrorPrinter.printError(method, subState.getTo(),
                    // superState.getTo());
                }
            }
        }
    }

    private static void createVariableInContext(
            String thisName,
            TypeChecker tc,
            RefinedFunction subFunction,
            RefinedFunction superFunction,
            CtParameter<?> ctParameter) {
        RefinedVariable rv = tc.getContext()
                .addVarToContext(
                        thisName,
                        Utils.getType(subFunction.getTargetClass(), tc.getFactory()),
                        new Predicate(),
                        ctParameter);
        rv.addSuperType(Utils.getType(superFunction.getTargetClass(), tc.getFactory())); // TODO: change: this only
        // works
        // for one superclass

    }

    /**
     * Changes all variable names in c to match the names of superFunction
     *
     * @param fromName
     * @param thisName
     * @param superFunction
     * @param subFunction
     * @param c
     *
     * @return
     */
    private static Predicate matchVariableNames(
            String fromName, String thisName, RefinedFunction superFunction, RefinedFunction subFunction, Predicate c) {
        Predicate nc = c.substituteVariable(fromName, thisName);
        List<Variable> superArgs = superFunction.getArguments();
        List<Variable> subArgs = subFunction.getArguments();
        for (int i = 0; i < subArgs.size(); i++) {
            nc.substituteVariable(subArgs.get(i).getName(), superArgs.get(i).getName());
        }
        return nc;
    }

    private static Predicate matchVariableNames(String fromName, String thisName, Predicate c) {
        return c.substituteVariable(fromName, thisName);
    }
}
