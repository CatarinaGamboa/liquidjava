package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.UnaryExpression;

public class ConstantFolding {

    public static Expression fold(Expression exp) {
        // recursively simplify in all children
        if (exp.hasChildren()) {
            for (int i = 0; i < exp.getChildren().size(); i++) {
                Expression child = exp.getChildren().get(i);
                Expression propagatedChild = fold(child);
                exp.setChild(i, propagatedChild);
            }
        }

        // try to fold the current expression
        if (exp instanceof BinaryExpression) {
            return foldBinaryExpression((BinaryExpression) exp);
        } else if (exp instanceof UnaryExpression) {
            return foldUnaryExpression((UnaryExpression) exp);
        }
        return exp;
    }

    private static Expression foldBinaryExpression(BinaryExpression binExp) {
        Expression left = binExp.getFirstOperand();
        Expression right = binExp.getSecondOperand();
        String op = binExp.getOperator();

        // arithmetic operations with integer literals
        if (left instanceof LiteralInt && right instanceof LiteralInt) {
            int l = ((LiteralInt) left).getValue();
            int r = ((LiteralInt) right).getValue();

            switch (op) {
                case "+":
                    return new LiteralInt(l + r);
                case "-":
                    return new LiteralInt(l - r);
                case "*":
                    return new LiteralInt(l * r);
                case "/":
                    if (r != 0)
                        return new LiteralInt(l / r);
                    break;
                case "%":
                    if (r != 0)
                        return new LiteralInt(l % r);
                    break;
                case "<":
                    return new LiteralBoolean(l < r);
                case "<=":
                    return new LiteralBoolean(l <= r);
                case ">":
                    return new LiteralBoolean(l > r);
                case ">=":
                    return new LiteralBoolean(l >= r);
                case "==":
                    return new LiteralBoolean(l == r);
                case "!=":
                    return new LiteralBoolean(l != r);
            }
        }

        // arithmetic operations with real literals
        else if (left instanceof LiteralReal && right instanceof LiteralReal) {
            double l = ((LiteralReal) left).getValue();
            double r = ((LiteralReal) right).getValue();
            switch (op) {
                case "+":
                    return new LiteralReal(l + r);
                case "-":
                    return new LiteralReal(l - r);
                case "*":
                    return new LiteralReal(l * r);
                case "/":
                    if (r != 0.0)
                        return new LiteralReal(l / r);
                    break;
                case "%":
                    if (r != 0.0)
                        return new LiteralReal(l % r);
                    break;
                case "<":
                    return new LiteralBoolean(l < r);
                case "<=":
                    return new LiteralBoolean(l <= r);
                case ">":
                    return new LiteralBoolean(l > r);
                case ">=":
                    return new LiteralBoolean(l >= r);
                case "==":
                    return new LiteralBoolean(l == r);
                case "!=":
                    return new LiteralBoolean(l != r);
            }
        }

        // mixed integer and real operations
        else if ((left instanceof LiteralInt && right instanceof LiteralReal) || (left instanceof LiteralReal && right instanceof LiteralInt)) {
            double l = left instanceof LiteralInt ? ((LiteralInt) left).getValue() : ((LiteralReal) left).getValue();
            double r = right instanceof LiteralInt ? ((LiteralInt) right).getValue() : ((LiteralReal) right).getValue();
            switch (op) {
                case "+":
                    return new LiteralReal(l + r);
                case "-":
                    return new LiteralReal(l - r);
                case "*":
                    return new LiteralReal(l * r);
                case "/":
                    if (r != 0.0)
                        return new LiteralReal(l / r);
                    break;
                case "%":
                    if (r != 0.0)
                        return new LiteralReal(l % r);
                    break;
                case "<":
                    return new LiteralBoolean(l < r);
                case "<=":
                    return new LiteralBoolean(l <= r);
                case ">":
                    return new LiteralBoolean(l > r);
                case ">=":
                    return new LiteralBoolean(l >= r);
                case "==":
                    return new LiteralBoolean(l == r);
                case "!=":
                    return new LiteralBoolean(l != r);
            }
        }

        // boolean operations with boolean literals
        else if (left instanceof LiteralBoolean && right instanceof LiteralBoolean) {
            boolean l = ((LiteralBoolean) left).isBooleanTrue();
            boolean r = ((LiteralBoolean) right).isBooleanTrue();
            switch (op) {
            case "&&":
                return new LiteralBoolean(l && r);
            case "||":
                return new LiteralBoolean(l || r);
            case "-->": // implication: !a || b
                return new LiteralBoolean(!l || r);
            case "==":
                return new LiteralBoolean(l == r);
            case "!=":
                return new LiteralBoolean(l != r);
            }
        }
        // no folding, return original
        return binExp;
    }

    private static Expression foldUnaryExpression(UnaryExpression unaryExp) {
        Expression operand = unaryExp.getChildren().get(0);
        String operator = unaryExp.getOp();
        if (operator.equals("!") && operand instanceof LiteralBoolean) {
            // !true -> false, !false -> true
            boolean value = ((LiteralBoolean) operand).isBooleanTrue();
            return new LiteralBoolean(!value);
        }
        return unaryExp;
    }
}
