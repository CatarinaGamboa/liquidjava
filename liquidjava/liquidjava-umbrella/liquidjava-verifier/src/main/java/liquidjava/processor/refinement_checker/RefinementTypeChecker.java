package liquidjava.processor.refinement_checker;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import liquidjava.errors.ErrorEmitter;
import liquidjava.errors.ErrorHandler;
import liquidjava.processor.context.*;
import liquidjava.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import liquidjava.processor.refinement_checker.general_checkers.OperationsChecker;
import liquidjava.processor.refinement_checker.object_checkers.AuxStateHandler;
import liquidjava.rj_language.BuiltinFunctionPredicate;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.specification.StateRefinement;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends TypeChecker {
    // This class should do the following:

    // 1. Keep track of the context variable types
    // 2. Do type checking and inference

    // Auxiliary TypeCheckers
    OperationsChecker otc;
    MethodsFunctionsChecker mfc;

    public RefinementTypeChecker(Context context, Factory factory, ErrorEmitter errorEmitter) {
        super(context, factory, errorEmitter);
        otc = new OperationsChecker(this);
        mfc = new MethodsFunctionsChecker(this);
        System.out.println("In RefinementTypeChecker");
    }

    // --------------------- Visitors -----------------------------------

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        if (errorEmitter.foundError()) {
            return;
        }

        // System.out.println("CTCLASS:"+ctClass.getSimpleName());
        context.reinitializeContext();
        super.visitCtClass(ctClass);
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (errorEmitter.foundError()) {
            return;
        }

        // System.out.println("CT INTERFACE: " +intrface.getSimpleName());
        if (getExternalRefinement(intrface).isPresent()) {
            return;
        }
        super.visitCtInterface(intrface);
    }

    @Override
    public <A extends Annotation> void visitCtAnnotationType(CtAnnotationType<A> annotationType) {
        if (errorEmitter.foundError()) {
            return;
        }
        super.visitCtAnnotationType(annotationType);
    }

    @Override
    public <T> void visitCtConstructor(CtConstructor<T> c) {
        if (errorEmitter.foundError()) {
            return;
        }

        context.enterContext();
        mfc.loadFunctionInfo(c);
        super.visitCtConstructor(c);
        context.exitContext();
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        if (errorEmitter.foundError()) {
            return;
        }

        context.enterContext();
        if (!method.getSignature().equals("main(java.lang.String[])")) {
            mfc.loadFunctionInfo(method);
        }
        super.visitCtMethod(method);
        context.exitContext();

    }

    @Override
    public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtLocalVariable(localVariable);
        // only declaration, no assignment
        if (localVariable.getAssignment() == null) {
            Optional<Predicate> a;
            try {
                a = getRefinementFromAnnotation(localVariable);
            } catch (ParsingException e) {
                return;// error already in ErrorEmitter
            }
            context.addVarToContext(localVariable.getSimpleName(), localVariable.getType(), a.orElse(new Predicate()),
                    localVariable);
        } else {
            String varName = localVariable.getSimpleName();
            CtExpression<?> e = localVariable.getAssignment();

            Predicate refinementFound = getRefinement(e);
            if (refinementFound == null) {
                refinementFound = new Predicate();
            }
            context.addVarToContext(varName, localVariable.getType(), new Predicate(), e);

            try {
                checkVariableRefinements(refinementFound, varName, localVariable.getType(), localVariable,
                        localVariable);
            } catch (ParsingException e1) {
                return;// error already in ErrorEmitter
            }

            AuxStateHandler.addStateRefinements(this, varName, e);
        }
    }

    @Override
    public <T> void visitCtNewArray(CtNewArray<T> newArray) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtNewArray(newArray);
        List<CtExpression<Integer>> l = newArray.getDimensionExpressions();
        // TODO only working for 1 dimension
        for (CtExpression<?> exp : l) {
            Predicate c;
            try {
                c = getExpressionRefinements(exp);
            } catch (ParsingException e) {
                return;// error already in ErrorEmitter
            }
            String name = String.format(freshFormat, context.getCounter());
            if (c.getVariableNames().contains(WILD_VAR)) {
                c = c.substituteVariable(WILD_VAR, name);
            } else {
                c = Predicate.createEquals(Predicate.createVar(name), c);
            }
            context.addVarToContext(name, factory.Type().INTEGER_PRIMITIVE, c, exp);
            Predicate ep;
            try {
                ep = Predicate.createEquals(
                        BuiltinFunctionPredicate.builtin_length(WILD_VAR, newArray, getErrorEmitter()),
                        Predicate.createVar(name));
            } catch (ParsingException e) {
                return;// error already in ErrorEmitter
            }
            newArray.putMetadata(REFINE_KEY, ep);
        }
    }

    @Override
    public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtThisAccess(thisAccess);
        CtClass<?> c = thisAccess.getParent(CtClass.class);
        String s = c.getSimpleName();
        if (thisAccess.getParent() instanceof CtReturn) {
            String thisName = String.format(thisFormat, s);
            thisAccess.putMetadata(REFINE_KEY,
                    Predicate.createEquals(Predicate.createVar(WILD_VAR), Predicate.createVar(thisName)));
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, A extends T> void visitCtAssignment(CtAssignment<T, A> assignement) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtAssignment(assignement);
        CtExpression<T> ex = assignement.getAssigned();

        if (ex instanceof CtVariableWriteImpl) {
            CtVariableReference<?> var = ((CtVariableAccess<?>) ex).getVariable();
            CtVariable<T> varDecl = (CtVariable<T>) var.getDeclaration();
            String name = var.getSimpleName();
            checkAssignment(name, varDecl.getType(), ex, assignement.getAssignment(), assignement, varDecl);

        } else if (ex instanceof CtFieldWrite) {
            CtFieldWrite<?> fw = ((CtFieldWrite<?>) ex);
            CtFieldReference<?> cr = fw.getVariable();
            CtField<?> f = fw.getVariable().getDeclaration();
            String updatedVarName = String.format(thisFormat, cr.getSimpleName());
            checkAssignment(updatedVarName, cr.getType(), ex, assignement.getAssignment(), assignement, f);
            // corresponding ghost function update
            CtMethod<?> transitionMethod = factory.createMethod();
            transitionMethod.setType(factory.createCtTypeReference(void.class));
            transitionMethod.setBody(factory.createCtBlock(factory.createCodeSnippetStatement()));
            transitionMethod.setSimpleName("_");

            transitionMethod.setParent(f.getDeclaringType());
            CtAnnotation<?> ann = factory.createAnnotation(factory.createCtTypeReference(StateRefinement.class));

            String stateChangeRefinementTo = f.getSimpleName() + "(this) == " + updatedVarName;
            String stateChangeRefinementFrom = "true";
            ann.addValue("to", stateChangeRefinementTo);
            ann.addValue("from", stateChangeRefinementFrom);

            transitionMethod.addAnnotation(ann);

            System.out.println("Target for invocation: " + fw.getTarget());
            CtInvocation<?> inv = factory.createInvocation(fw.getTarget(), transitionMethod.getReference(),
                    Collections.emptyList());

            RefinedFunction rf = new RefinedFunction();
            rf.setName(transitionMethod.getSimpleName());
            rf.setType(transitionMethod.getType());
            rf.setRefReturn(new Predicate());
            rf.setClass(transitionMethod.getDeclaringType().getQualifiedName());

            try {
                AuxStateHandler.handleMethodState(transitionMethod, rf, this);
                AuxStateHandler.checkTargetChanges(this, rf, inv.getTarget(), Collections.emptyMap(), inv);
            } catch (ParsingException e) {
                ErrorHandler.printCostumeError(assignement, "ParsingException in `" + assignement + "` in class `"
                        + f.getDeclaringType() + "` : " + e.getMessage(), errorEmitter);

                return;
            }

        }
        if (ex instanceof CtArrayWrite) {
            // Predicate c = getRefinement(ex);
            // TODO continue
            // c.substituteVariable(WILD_VAR, );
        }
    }

    @Override
    public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtArrayRead(arrayRead);
        String name = String.format(instanceFormat, "arrayAccess", context.getCounter());
        context.addVarToContext(name, arrayRead.getType(), new Predicate(), arrayRead);
        arrayRead.putMetadata(REFINE_KEY, Predicate.createVar(name));
        // TODO predicate for now is always TRUE
    }

    @Override
    public <T> void visitCtLiteral(CtLiteral<T> lit) {
        if (errorEmitter.foundError()) {
            return;
        }

        List<String> types = Arrays.asList(implementedTypes);
        String type = lit.getType().getQualifiedName();
        if (types.contains(type)) {
            lit.putMetadata(REFINE_KEY, Predicate.createEquals(Predicate.createVar(WILD_VAR),
                    Predicate.createLit(lit.getValue().toString(), type)));

        } else if (lit.getType().getQualifiedName().contentEquals("java.lang.String")) {
            // Only taking care of strings inside refinements
        } else {
            System.out.println(String.format("Literal of type %s not implemented:", lit.getType().getQualifiedName()));
        }
    }

    @Override
    public <T> void visitCtField(CtField<T> f) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtField(f);
        Optional<Predicate> c;
        try {
            c = getRefinementFromAnnotation(f);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        // context.addVarToContext(f.getSimpleName(), f.getType(),
        // c.map( i -> i.substituteVariable(WILD_VAR, f.getSimpleName()).orElse(new Predicate()) );
        String nname = String.format(thisFormat, f.getSimpleName());
        Predicate ret = new Predicate();
        if (c.isPresent()) {
            ret = c.get().substituteVariable(WILD_VAR, nname).substituteVariable(f.getSimpleName(), nname);
        }
        RefinedVariable v = context.addVarToContext(nname, f.getType(), ret, f);
        if (v instanceof Variable) {
            ((Variable) v).setLocation("this");
        }

    }

    @Override
    public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
        if (errorEmitter.foundError()) {
            return;
        }

        String fieldName = fieldRead.getVariable().getSimpleName();
        if (context.hasVariable(fieldName)) {
            RefinedVariable rv = context.getVariableByName(fieldName);
            if (rv instanceof Variable && ((Variable) rv).getLocation().isPresent()
                    && ((Variable) rv).getLocation().get().equals(fieldRead.getTarget().toString())) {
                fieldRead.putMetadata(REFINE_KEY, context.getVariableRefinements(fieldName));
            } else {
                fieldRead.putMetadata(REFINE_KEY,
                        Predicate.createEquals(Predicate.createVar(WILD_VAR), Predicate.createVar(fieldName)));
            }

        } else if (context.hasVariable(String.format(thisFormat, fieldName))) {
            String thisName = String.format(thisFormat, fieldName);
            fieldRead.putMetadata(REFINE_KEY,
                    Predicate.createEquals(Predicate.createVar(WILD_VAR), Predicate.createVar(thisName)));

        } else if (fieldRead.getVariable().getSimpleName().equals("length")) {
            String targetName = fieldRead.getTarget().toString();
            try {
                fieldRead.putMetadata(REFINE_KEY, Predicate.createEquals(Predicate.createVar(WILD_VAR),
                        BuiltinFunctionPredicate.builtin_length(targetName, fieldRead, getErrorEmitter())));
            } catch (ParsingException e) {
                return;// error already in ErrorEmitter
            }
        } else {
            fieldRead.putMetadata(REFINE_KEY, new Predicate());
            // TODO DO WE WANT THIS OR TO SHOW ERROR MESSAGE
        }

        super.visitCtFieldRead(fieldRead);
    }

    @Override
    public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtVariableRead(variableRead);
        CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
        getPutVariableMetadada(variableRead, varDecl.getSimpleName());
    }

    /**
     * Visitor for binary operations Adds metadata to the binary operations from the operands
     */
    @Override
    public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtBinaryOperator(operator);
        try {
            otc.getBinaryOpRefinements(operator);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }

    }

    @Override
    public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtUnaryOperator(operator);
        try {
            otc.getUnaryOpRefinements(operator);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }

    }

    public <R> void visitCtInvocation(CtInvocation<R> invocation) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtInvocation(invocation);
        mfc.getInvocationRefinements(invocation);
        System.out.println();
    }

    @Override
    public <R> void visitCtReturn(CtReturn<R> ret) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtReturn(ret);
        mfc.getReturnRefinements(ret);

    }

    @Override
    public void visitCtIf(CtIf ifElement) {
        if (errorEmitter.foundError()) {
            return;
        }

        CtExpression<Boolean> exp = ifElement.getCondition();

        Predicate expRefs;
        try {
            expRefs = getExpressionRefinements(exp);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        String freshVarName = String.format(freshFormat, context.getCounter());
        expRefs = expRefs.substituteVariable(WILD_VAR, freshVarName);
        Predicate lastExpRefs = substituteAllVariablesForLastInstance(expRefs);
        expRefs = Predicate.createConjunction(expRefs, lastExpRefs);

        // TODO Change in future
        if (expRefs.getVariableNames().contains("null")) {
            expRefs = new Predicate();
        }

        RefinedVariable freshRV = context.addInstanceToContext(freshVarName, factory.Type().INTEGER_PRIMITIVE, expRefs,
                exp);
        vcChecker.addPathVariable(freshRV);

        context.variablesNewIfCombination();
        context.variablesSetBeforeIf();
        context.enterContext();

        // VISIT THEN
        context.enterContext();
        visitCtBlock(ifElement.getThenStatement());
        context.variablesSetThenIf();
        context.exitContext();

        // VISIT ELSE
        if (ifElement.getElseStatement() != null) {
            context.getVariableByName(freshVarName);
            // expRefs = expRefs.negate();
            context.newRefinementToVariableInContext(freshVarName, expRefs.negate());

            context.enterContext();
            visitCtBlock(ifElement.getElseStatement());
            context.variablesSetElseIf();
            context.exitContext();
        }
        // end
        vcChecker.removePathVariable(freshRV);
        context.exitContext();
        context.variablesCombineFromIf(expRefs);
        context.variablesFinishIfCombination();
    }

    @Override
    public <T> void visitCtArrayWrite(CtArrayWrite<T> arrayWrite) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtArrayWrite(arrayWrite);
        CtExpression<?> index = arrayWrite.getIndexExpression();
        BuiltinFunctionPredicate fp;
        try {
            fp = BuiltinFunctionPredicate.builtin_addToIndex(arrayWrite.getTarget().toString(), index.toString(),
                    WILD_VAR, arrayWrite, getErrorEmitter());
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }
        arrayWrite.putMetadata(REFINE_KEY, fp);
        // TODO fazer mais...? faz sentido
    }

    @Override
    public <T> void visitCtConditional(CtConditional<T> conditional) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtConditional(conditional);
        Predicate cond = getRefinement(conditional.getCondition());
        Predicate c = Predicate.createITE(cond, getRefinement(conditional.getThenExpression()),
                getRefinement(conditional.getElseExpression()));
        conditional.putMetadata(REFINE_KEY, c);

    }

    @Override
    public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtConstructorCall(ctConstructorCall);
        mfc.getConstructorInvocationRefinements(ctConstructorCall);
    }

    @Override
    public <T> void visitCtNewClass(CtNewClass<T> newClass) {
        if (errorEmitter.foundError()) {
            return;
        }

        super.visitCtNewClass(newClass);
        System.out.println("new class");
    }

    // ############################### Inner Visitors ##########################################
    private void checkAssignment(String name, CtTypeReference<?> type, CtExpression<?> ex, CtExpression<?> assignment,
            CtElement parentElem, CtElement varDecl) {
        getPutVariableMetadada(ex, name);

        Predicate refinementFound = getRefinement(assignment);
        if (refinementFound == null) {
            RefinedVariable rv = context.getVariableByName(name);
            if (rv instanceof Variable) {
                refinementFound = rv.getMainRefinement();
            } else {
                refinementFound = new Predicate();
            }
        }
        Optional<VariableInstance> r = context.getLastVariableInstance(name);
        if (r.isPresent()) {
            vcChecker.removePathVariableThatIncludes(r.get().getName());// AQUI!!
        }

        vcChecker.removePathVariableThatIncludes(name);// AQUI!!
        try {
            checkVariableRefinements(refinementFound, name, type, parentElem, varDecl);
        } catch (ParsingException e) {
            return;// error already in ErrorEmitter
        }

    }

    private Predicate getExpressionRefinements(CtExpression<?> element) throws ParsingException {
        if (element instanceof CtVariableRead<?>) {
            // CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
            return getRefinement(element);
        } else if (element instanceof CtBinaryOperator<?>) {
            CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
            visitCtBinaryOperator(binop);
            return getRefinement(binop);
        } else if (element instanceof CtUnaryOperator<?>) {
            CtUnaryOperator<?> op = (CtUnaryOperator<?>) element;
            visitCtUnaryOperator(op);
            return getRefinement(op);
        } else if (element instanceof CtLiteral<?>) {
            CtLiteral<?> l = (CtLiteral<?>) element;
            return new Predicate(l.getValue().toString(), l, errorEmitter);
        } else if (element instanceof CtInvocation<?>) {
            CtInvocation<?> inv = (CtInvocation<?>) element;
            visitCtInvocation(inv);
            return getRefinement(inv);
        }
        return getRefinement(element);
    }

    private Predicate substituteAllVariablesForLastInstance(Predicate c) {
        Predicate ret = c;
        List<String> ls = c.getVariableNames();
        for (String s : ls) {
            Optional<VariableInstance> rv = context.getLastVariableInstance(s);
            if (rv.isPresent()) {
                VariableInstance vi = rv.get();
                ret = ret.substituteVariable(s, vi.getName());
            }
        }
        return ret;
    }

    // ############################### Get Metadata ##########################################

    /**
     * @param <T>
     * @param elem
     *            Cannot be null
     */
    private <T> void getPutVariableMetadada(CtElement elem, String name) {
        Predicate cref = Predicate.createEquals(Predicate.createVar(WILD_VAR), Predicate.createVar(name));
        Optional<VariableInstance> ovi = context.getLastVariableInstance(name);
        if (ovi.isPresent()) {
            cref = Predicate.createEquals(Predicate.createVar(WILD_VAR), Predicate.createVar(ovi.get().getName()));
        }

        elem.putMetadata(REFINE_KEY, cref);
    }

}
