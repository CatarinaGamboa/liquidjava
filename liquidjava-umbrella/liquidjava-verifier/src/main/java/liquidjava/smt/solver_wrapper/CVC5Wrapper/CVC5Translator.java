package liquidjava.smt.solver_wrapper;

import com.microsoft.z3.Expr;
import com.microsoft.z3.FPExpr;
import liquidjava.processor.context.Context;

public class TranslatorToCVC5 extends SMTWrapper {

    TranslatorToCVC5(Context c) {

    }

    @Override
    public Status verifyExpression(ExprWrapper e) throws Exception {
        return null;
    }

    @Override
    public ExprWrapper makeIntegerLiteral(int value) {
        return null;
    }

    @Override
    public ExprWrapper makeLongLiteral(long value) {
        return null;
    }

    @Override
    public ExprWrapper makeDoubleLiteral(double value) {
        return null;
    }

    @Override
    public ExprWrapper makeString(String s) {
        return null;
    }

    @Override
    public ExprWrapper makeBooleanLiteral(boolean value) {
        return null;
    }

    @Override
    public ExprWrapper makeFunctionInvocation(String name, ExprWrapper[] params) throws Exception {
        return null;
    }

    @Override
    public ExprWrapper makeSelect(String name, ExprWrapper[] params) {
        return null;
    }

    @Override
    public ExprWrapper makeStore(String name, ExprWrapper[] params) {
        return null;
    }

    @Override
    public ExprWrapper makeEquals(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeLt(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeLtEq(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeGt(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeGtEq(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeImplies(ExprWrapper e1, ExprWrapper e2) {
        return null;
    }

    @Override
    public ExprWrapper makeBiconditional(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeAnd(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper mkNot(ExprWrapper e1) {
        return null;
    }

    @Override
    public ExprWrapper makeOr(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeMinus(ExprWrapper eval) {
        return null;
    }

    @Override
    public ExprWrapper makeAdd(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeSub(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeMul(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeDiv(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    @Override
    public ExprWrapper makeMod(ExprWrapper eval, ExprWrapper eval2) {
        return null;
    }

    public FPExpr toFP(ExprWrapper e) {
        return null;
    }

    @Override
    public ExprWrapper makeIte(ExprWrapper c, ExprWrapper t, ExprWrapper e) {
        return null;
    }
}
