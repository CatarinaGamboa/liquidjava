package liquidjava.processor.refinement_checker;

import static liquidjava.diagnostics.Diagnostics.diagnostics;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import liquidjava.processor.context.*;
import liquidjava.processor.refinement_checker.general_checkers.MethodsFunctionsChecker;
import liquidjava.processor.refinement_checker.general_checkers.OperationsChecker;
import liquidjava.processor.refinement_checker.object_checkers.AuxStateHandler;
import liquidjava.rj_language.BuiltinFunctionPredicate;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.parsing.ParsingException;
import liquidjava.utils.constants.Formats;
import liquidjava.utils.constants.Keys;
import liquidjava.utils.constants.Types;

import org.apache.commons.lang3.NotImplementedException;
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

    public RefinementTypeChecker(Context context, Factory factory) {
        super(context, factory);
        otc = new OperationsChecker(this);
        mfc = new MethodsFunctionsChecker(this);
    }

    // --------------------- Visitors -----------------------------------

    @Override
    public <T> void visitCtClass(CtClass<T> ctClass) {
        if (diagnostics.foundError()) {
            return;
        }

        // System.out.println("CTCLASS:"+ctClass.getSimpleName());
        context.reinitializeContext();
        super.visitCtClass(ctClass);
    }

    @Override
    public <T> void visitCtInterface(CtInterface<T> intrface) {
        if (diagnostics.foundError()) {
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
        if (diagnostics.foundError()) {
            return;
        }
        super.visitCtAnnotationType(annotationType);
    }

    @Override
    public <T> void visitCtConstructor(CtConstructor<T> c) {
        if (diagnostics.foundError()) {
            return;
        }

        context.enterContext();
        mfc.loadFunctionInfo(c);
        super.visitCtConstructor(c);
        context.exitContext();
    }

    public <R> void visitCtMethod(CtMethod<R> method) {
        if (diagnostics.foundError()) {
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
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtLocalVariable(localVariable);
        // only declaration, no assignment
        if (localVariable.getAssignment() == null) {
            Optional<Predicate> a;
            try {
                a = getRefinementFromAnnotation(localVariable);
            } catch (ParsingException e) {
                return; // error already reported
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
                return; // error already reported
            }

            AuxStateHandler.addStateRefinements(this, varName, e);
        }
    }

    @Override
    public <T> void visitCtNewArray(CtNewArray<T> newArray) {
        if (diagnostics.foundError()) {
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
                return; // error already reported
            }
            String name = String.format(Formats.FRESH, context.getCounter());
            if (c.getVariableNames().contains(Keys.WILDCARD)) {
                c = c.substituteVariable(Keys.WILDCARD, name);
            } else {
                c = Predicate.createEquals(Predicate.createVar(name), c);
            }
            context.addVarToContext(name, factory.Type().INTEGER_PRIMITIVE, c, exp);
            Predicate ep;
            try {
                ep = Predicate.createEquals(BuiltinFunctionPredicate.length(Keys.WILDCARD, newArray),
                        Predicate.createVar(name));
            } catch (ParsingException e) {
                return; // error already reported
            }
            newArray.putMetadata(Keys.REFINEMENT, ep);
        }
    }

    @Override
    public <T> void visitCtThisAccess(CtThisAccess<T> thisAccess) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtThisAccess(thisAccess);
        CtClass<?> c = thisAccess.getParent(CtClass.class);
        String s = c.getSimpleName();
        if (thisAccess.getParent() instanceof CtReturn) {
            String thisName = String.format(Formats.THIS, s);
            thisAccess.putMetadata(Keys.REFINEMENT,
                    Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), Predicate.createVar(thisName)));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, A extends T> void visitCtAssignment(CtAssignment<T, A> assignment) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtAssignment(assignment);
        CtExpression<T> ex = assignment.getAssigned();

        if (ex instanceof CtVariableWriteImpl) {
            CtVariableReference<?> var = ((CtVariableAccess<?>) ex).getVariable();
            CtVariable<T> varDecl = (CtVariable<T>) var.getDeclaration();
            String name = var.getSimpleName();
            checkAssignment(name, varDecl.getType(), ex, assignment.getAssignment(), assignment, varDecl);

        } else if (ex instanceof CtFieldWrite) {
            CtFieldWrite<?> fw = ((CtFieldWrite<?>) ex);
            CtFieldReference<?> cr = fw.getVariable();
            CtField<?> f = fw.getVariable().getDeclaration();
            String updatedVarName = String.format(Formats.THIS, cr.getSimpleName());
            checkAssignment(updatedVarName, cr.getType(), ex, assignment.getAssignment(), assignment, f);

            // corresponding ghost function update
            if (fw.getVariable().getType().toString().equals("int")) {
                AuxStateHandler.updateGhostField(fw, this);
            }
        }
        // if (ex instanceof CtArrayWrite) {
        // Predicate c = getRefinement(ex);
        // TODO continue
        // c.substituteVariable(WILD_VAR, );
        // }
    }

    @Override
    public <T> void visitCtArrayRead(CtArrayRead<T> arrayRead) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtArrayRead(arrayRead);
        String name = String.format(Formats.INSTANCE, "arrayAccess", context.getCounter());
        context.addVarToContext(name, arrayRead.getType(), new Predicate(), arrayRead);
        arrayRead.putMetadata(Keys.REFINEMENT, Predicate.createVar(name));
        // TODO predicate for now is always TRUE
    }

    @Override
    public <T> void visitCtLiteral(CtLiteral<T> lit) {
        if (diagnostics.foundError()) {
            return;
        }

        List<String> types = Arrays.asList(Types.IMPLEMENTED);
        String type = lit.getType().getQualifiedName();
        if (types.contains(type)) {
            lit.putMetadata(Keys.REFINEMENT, Predicate.createEquals(Predicate.createVar(Keys.WILDCARD),
                    Predicate.createLit(lit.getValue().toString(), type)));

        } else if (lit.getType().getQualifiedName().equals("java.lang.String")) {
            // Only taking care of strings inside refinements
        } else if (type.equals(Types.NULL)) {
            // Skip null literals
        } else {
            throw new NotImplementedException(
                    String.format("Literal of type %s not implemented:", lit.getType().getQualifiedName()));
        }
    }

    @Override
    public <T> void visitCtField(CtField<T> f) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtField(f);
        Optional<Predicate> c;
        try {
            c = getRefinementFromAnnotation(f);
        } catch (ParsingException e) {
            return; // error already reported
        }
        // context.addVarToContext(f.getSimpleName(), f.getType(),
        // c.map( i -> i.substituteVariable(WILD_VAR, f.getSimpleName()).orElse(new
        // Predicate()) );
        String nname = String.format(Formats.THIS, f.getSimpleName());
        Predicate ret = new Predicate();
        if (c.isPresent()) {
            ret = c.get().substituteVariable(Keys.WILDCARD, nname).substituteVariable(f.getSimpleName(), nname);
        }
        RefinedVariable v = context.addVarToContext(nname, f.getType(), ret, f);
        if (v instanceof Variable) {
            ((Variable) v).setLocation("this");
        }
    }

    @Override
    public <T> void visitCtFieldRead(CtFieldRead<T> fieldRead) {
        if (diagnostics.foundError()) {
            return;
        }

        String fieldName = fieldRead.getVariable().getSimpleName();
        if (context.hasVariable(fieldName)) {
            RefinedVariable rv = context.getVariableByName(fieldName);
            if (rv instanceof Variable && ((Variable) rv).getLocation().isPresent()
                    && ((Variable) rv).getLocation().get().equals(fieldRead.getTarget().toString())) {
                fieldRead.putMetadata(Keys.REFINEMENT, context.getVariableRefinements(fieldName));
            } else {
                fieldRead.putMetadata(Keys.REFINEMENT,
                        Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), Predicate.createVar(fieldName)));
            }

        } else if (context.hasVariable(String.format(Formats.THIS, fieldName))) {
            String thisName = String.format(Formats.THIS, fieldName);
            fieldRead.putMetadata(Keys.REFINEMENT,
                    Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), Predicate.createVar(thisName)));

        } else if (fieldRead.getVariable().getSimpleName().equals("length")) {
            String targetName = fieldRead.getTarget().toString();
            try {
                fieldRead.putMetadata(Keys.REFINEMENT, Predicate.createEquals(Predicate.createVar(Keys.WILDCARD),
                        BuiltinFunctionPredicate.length(targetName, fieldRead)));
            } catch (ParsingException e) {
                return; // error already reported
            }
        } else {
            fieldRead.putMetadata(Keys.REFINEMENT, new Predicate());
            // TODO DO WE WANT THIS OR TO SHOW ERROR MESSAGE
        }

        super.visitCtFieldRead(fieldRead);
    }

    @Override
    public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
        if (diagnostics.foundError()) {
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
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtBinaryOperator(operator);
        try {
            otc.getBinaryOpRefinements(operator);
        } catch (ParsingException e) {
            return; // error already reported
        }
    }

    @Override
    public <T> void visitCtUnaryOperator(CtUnaryOperator<T> operator) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtUnaryOperator(operator);
        try {
            otc.getUnaryOpRefinements(operator);
        } catch (ParsingException e) {
            return; // error already reported
        }
    }

    public <R> void visitCtInvocation(CtInvocation<R> invocation) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtInvocation(invocation);
        mfc.getInvocationRefinements(invocation);
    }

    @Override
    public <R> void visitCtReturn(CtReturn<R> ret) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtReturn(ret);
        mfc.getReturnRefinements(ret);
    }

    @Override
    public void visitCtIf(CtIf ifElement) {
        if (diagnostics.foundError()) {
            return;
        }

        CtExpression<Boolean> exp = ifElement.getCondition();

        Predicate expRefs;
        try {
            expRefs = getExpressionRefinements(exp);
        } catch (ParsingException e) {
            return; // error already reported
        }
        String freshVarName = String.format(Formats.FRESH, context.getCounter());
        expRefs = expRefs.substituteVariable(Keys.WILDCARD, freshVarName);
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
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtArrayWrite(arrayWrite);
        CtExpression<?> index = arrayWrite.getIndexExpression();
        BuiltinFunctionPredicate fp;
        try {
            fp = BuiltinFunctionPredicate.addToIndex(arrayWrite.getTarget().toString(), index.toString(), Keys.WILDCARD,
                    arrayWrite);
        } catch (ParsingException e) {
            return; // error already reported
        }
        arrayWrite.putMetadata(Keys.REFINEMENT, fp);
    }

    @Override
    public <T> void visitCtConditional(CtConditional<T> conditional) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtConditional(conditional);
        Predicate cond = getRefinement(conditional.getCondition());
        Predicate c = Predicate.createITE(cond, getRefinement(conditional.getThenExpression()),
                getRefinement(conditional.getElseExpression()));
        conditional.putMetadata(Keys.REFINEMENT, c);
    }

    @Override
    public <T> void visitCtConstructorCall(CtConstructorCall<T> ctConstructorCall) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtConstructorCall(ctConstructorCall);
        mfc.getConstructorInvocationRefinements(ctConstructorCall);
    }

    @Override
    public <T> void visitCtNewClass(CtNewClass<T> newClass) {
        if (diagnostics.foundError()) {
            return;
        }

        super.visitCtNewClass(newClass);
    }

    // ############################### Inner Visitors
    // ##########################################
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
        // AQUI!!
        r.ifPresent(variableInstance -> vcChecker.removePathVariableThatIncludes(variableInstance.getName()));

        vcChecker.removePathVariableThatIncludes(name); // AQUI!!
        try {
            checkVariableRefinements(refinementFound, name, type, parentElem, varDecl);
        } catch (ParsingException e) {
            return; // error already reported
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
            return new Predicate(l.getValue().toString(), l);
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

    // ############################### Get Metadata
    // ##########################################

    /**
     * @param <T>
     * @param elem
     * @param name
     *            Cannot be null
     */
    private <T> void getPutVariableMetadada(CtElement elem, String name) {
        Predicate cref = Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), Predicate.createVar(name));
        Optional<VariableInstance> ovi = context.getLastVariableInstance(name);
        if (ovi.isPresent()) {
            cref = Predicate.createEquals(Predicate.createVar(Keys.WILDCARD), Predicate.createVar(ovi.get().getName()));
        }

        elem.putMetadata(Keys.REFINEMENT, cref);
    }
}
