package liquidjava.rj_language.visitors;

import liquidjava.rj_language.ast.*;

public interface ExpressionVisitor {
    void visitAliasInvocation(AliasInvocation ai) throws Exception;

    void visitBinaryExpression(BinaryExpression be) throws Exception;

    void visitFunctionInvocation(FunctionInvocation fi) throws Exception;

    void visitGroupExpression(GroupExpression ge) throws Exception;

    void visitITE(Ite ite) throws Exception;

    void visitLiteralBoolean(LiteralBoolean lb);

    void visitLiteralInt(LiteralInt li);

    void visitLiteralReal(LiteralReal lr) throws Exception;

    void visitLiteralString(LiteralString ls);

    void visitUnaryExpression(UnaryExpression ue) throws Exception;

    void visitVar(Var v) throws Exception;
}
