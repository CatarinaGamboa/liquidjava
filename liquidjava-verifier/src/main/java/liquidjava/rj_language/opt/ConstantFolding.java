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
        }
        if (exp instanceof UnaryExpression) {
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

            return switch (op) {
                case "+" -> new LiteralInt(l + r);
                case "-" -> new LiteralInt(l - r);
                case "*" -> new LiteralInt(l * r);
                case "/" -> r != 0 ? new LiteralInt(l / r) : binExp;
                case "%" -> r != 0 ? new LiteralInt(l % r) : binExp;
                case "<" -> new LiteralBoolean(l < r);
                case "<=" -> new LiteralBoolean(l <= r);
                case ">" -> new LiteralBoolean(l > r);
                case ">=" -> new LiteralBoolean(l >= r);
                case "==" -> new LiteralBoolean(l == r);
                case "!=" -> new LiteralBoolean(l != r);
                default -> binExp;
            };
        }

        // arithmetic operations with real literals
        if (left instanceof LiteralReal && right instanceof LiteralReal) {
            double l = ((LiteralReal) left).getValue();
            double r = ((LiteralReal) right).getValue();
            return switch (op) {
                case "+" -> new LiteralReal(l + r);
                case "-" -> new LiteralReal(l - r);
                case "*" -> new LiteralReal(l * r);
                case "/" -> r != 0.0 ? new LiteralReal(l / r) : binExp;
                case "%" -> r != 0.0 ? new LiteralReal(l % r) : binExp;
                case "<" -> new LiteralBoolean(l < r);
                case "<=" -> new LiteralBoolean(l <= r);
                case ">" -> new LiteralBoolean(l > r);
                case ">=" -> new LiteralBoolean(l >= r);
                case "==" -> new LiteralBoolean(l == r);
                case "!=" -> new LiteralBoolean(l != r);
                default -> binExp;
            };
        }

        // mixed integer and real operations
        if ((left instanceof LiteralInt && right instanceof LiteralReal) || (left instanceof LiteralReal && right instanceof LiteralInt)) {
            double l = left instanceof LiteralInt ? ((LiteralInt) left).getValue() : ((LiteralReal) left).getValue();
            double r = right instanceof LiteralInt ? ((LiteralInt) right).getValue() : ((LiteralReal) right).getValue();
            return switch (op) {
                case "+" -> new LiteralReal(l + r);
                case "-" -> new LiteralReal(l - r);
                case "*" -> new LiteralReal(l * r);
                case "/" -> r != 0.0 ? new LiteralReal(l / r) : binExp;
                case "%" -> r != 0.0 ? new LiteralReal(l % r) : binExp;
                case "<" -> new LiteralBoolean(l < r);
                case "<=" -> new LiteralBoolean(l <= r);
                case ">" -> new LiteralBoolean(l > r);
                case ">=" -> new LiteralBoolean(l >= r);
                case "==" -> new LiteralBoolean(l == r);
                case "!=" -> new LiteralBoolean(l != r);
                default -> binExp;
            };
        }

        // boolean operations with boolean literals
        if (left instanceof LiteralBoolean && right instanceof LiteralBoolean) {
            boolean l = ((LiteralBoolean) left).isBooleanTrue();
            boolean r = ((LiteralBoolean) right).isBooleanTrue();
            return switch (op) {
                case "&&" -> new LiteralBoolean(l && r);
                case "||" -> new LiteralBoolean(l || r);
                case "-->" -> new LiteralBoolean(!l || r);
                case "==" -> new LiteralBoolean(l == r);
                case "!=" -> new LiteralBoolean(l != r);
                default -> binExp;
            };
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
        if (operator.equals("-")) {
            // -(x) = -x
            if (operand instanceof LiteralInt) {
                int value = ((LiteralInt) operand).getValue();
                return new LiteralInt(-value);
            }
            if (operand instanceof LiteralReal) {
                double value = ((LiteralReal) operand).getValue();
                return new LiteralReal(-value);
            }
        }
        return unaryExp;
    }
}
