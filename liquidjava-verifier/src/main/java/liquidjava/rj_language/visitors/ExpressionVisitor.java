package liquidjava.rj_language.visitors;

import liquidjava.rj_language.ast.AliasInvocation;
import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.FunctionInvocation;
import liquidjava.rj_language.ast.GroupExpression;
import liquidjava.rj_language.ast.Ite;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.LiteralString;
import liquidjava.rj_language.ast.UnaryExpression;
import liquidjava.rj_language.ast.Var;

public interface ExpressionVisitor<T> {
    T visitAliasInvocation(AliasInvocation alias) throws Exception;

    T visitBinaryExpression(BinaryExpression exp) throws Exception;

    T visitFunctionInvocation(FunctionInvocation fun) throws Exception;

    T visitGroupExpression(GroupExpression exp) throws Exception;

    T visitIte(Ite ite) throws Exception;

    T visitLiteralInt(LiteralInt lit) throws Exception;

    T visitLiteralBoolean(LiteralBoolean lit) throws Exception;

    T visitLiteralReal(LiteralReal lit) throws Exception;

    T visitLiteralString(LiteralString lit) throws Exception;

    T visitUnaryExpression(UnaryExpression exp) throws Exception;

    T visitVar(Var var) throws Exception;
}