package liquidjava.rj_language.visitors;

import liquidjava.rj_language.ast.*;

public abstract class AbstractExpressionVisitor implements ExpressionVisitor {
    @Override
    public void visitAliasInvocation(AliasInvocation ai) throws Exception {
        for (Expression a : ai.getArgs()) {
            a.accept(this);
        }
    }

    @Override
    public void visitBinaryExpression(BinaryExpression be) throws Exception {
        be.getFirstOperand().accept(this);
        be.getSecondOperand().accept(this);
    }

    @Override
    public void visitFunctionInvocation(FunctionInvocation fi) throws Exception {
        for (Expression a : fi.getArgs()) {
            a.accept(this);
        }
    }

    @Override
    public void visitGroupExpression(GroupExpression ge) throws Exception {
        ge.getExpression().accept(this);
    }

    @Override
    public void visitITE(Ite ite) throws Exception {
        ite.getCondition().accept(this);
        ite.getThen().accept(this);
        ite.getElse().accept(this);
    }

    @Override
    public void visitLiteralBoolean(LiteralBoolean lb) {
    }

    @Override
    public void visitLiteralInt(LiteralInt li) {
    }

    @Override
    public void visitLiteralReal(LiteralReal lr) throws Exception {
    }

    @Override
    public void visitLiteralString(LiteralString ls) {
    }

    @Override
    public void visitUnaryExpression(UnaryExpression ue) throws Exception {
        ue.getExpression().accept(this);
    }

    @Override
    public void visitVar(Var v) throws Exception {
    }
}
