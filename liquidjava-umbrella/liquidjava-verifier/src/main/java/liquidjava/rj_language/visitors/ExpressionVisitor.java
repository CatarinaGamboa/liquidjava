package liquidjava.rj_language.visitors;

import liquidjava.rj_language.ast.*;

public interface ExpressionVisitor {
    void visitAliasInvocation(AliasInvocation ai);
    void visitBinaryExpression(BinaryExpression be);
    void visitFunctionInvocation(FunctionInvocation fi);
    void visitGroupExpression(GroupExpression ge);
    void visitITE(Ite ite);
    void visitLiteralBoolean(LiteralBoolean lb);
    void visitLiteralInt(LiteralInt li);
    void visitLiteralReal(LiteralReal lr);
    void visitLiteralString(LiteralString ls);
    void visitUnaryExpression(UnaryExpression ue);
    void visitVar(Var v);
}
