package liquidjava.rj_language.visitors;

import liquidjava.rj_language.ast.*;

public class AbstractExpressionVisitor implements ExpressionVisitor{
    @Override
    public void visitAliasInvocation(AliasInvocation ai) {
        ai.getArgs().forEach(a -> a.accept(this));
    }

    @Override
    public void visitBinaryExpression(BinaryExpression be) {
        be.getFirstOperand().accept(this);
        be.getSecondOperand().accept(this);
    }

    @Override
    public void visitFunctionInvocation(FunctionInvocation fi) {
        fi.getArgs().forEach(a -> a.accept(this));
    }

    @Override
    public void visitGroupExpression(GroupExpression ge) {
        ge.getExpression().accept(this);
    }

    @Override
    public void visitITE(Ite ite) {
        ite.getCondition().accept(this);
        ite.getThen().accept(this);
        ite.getElse().accept(this);
    }

    @Override
    public void visitLiteralBoolean(LiteralBoolean lb) {}

    @Override
    public void visitLiteralInt(LiteralInt li) {}

    @Override
    public void visitLiteralReal(LiteralReal lr) {}

    @Override
    public void visitLiteralString(LiteralString ls) {}

    @Override
    public void visitUnaryExpression(UnaryExpression ue) {
        ue.getExpression().accept(this);
    }

    @Override
    public void visitVar(Var v) {}
}
