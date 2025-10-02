package liquidjava.rj_language.opt;

import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.GroupExpression;
import liquidjava.rj_language.ast.Ite;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralReal;
import liquidjava.rj_language.ast.UnaryExpression;

public class LogicSimplifier {

    public static Expression simplify(Expression exp) {
        // recursively simplify in all children
        if (exp.hasChildren()) {
            for (int i = 0; i < exp.getChildren().size(); i++) {
                Expression child = exp.getChildren().get(i);
                Expression propagatedChild = simplify(child);
                exp.setChild(i, propagatedChild);
            }
        }

        // apply simplification rules to the current expression
        if (exp instanceof BinaryExpression) {
            return simplifyBinaryExpression((BinaryExpression) exp);
        } else if (exp instanceof UnaryExpression) {
            return simplifyUnaryExpression((UnaryExpression) exp);
        } else if (exp instanceof Ite) {
            return simplifyIte((Ite) exp);
        } else if (exp instanceof GroupExpression) {
            return simplifyGroupExpression((GroupExpression) exp);
        }

        // literals, variables and function invocations cant be simplified
        return exp;
    }

    private static Expression simplifyBinaryExpression(BinaryExpression binExp) {
        Expression left = binExp.getFirstOperand();
        Expression right = binExp.getSecondOperand();
        String operator = binExp.getOperator();

        switch (operator) {
            // logical simplifications
            case "&&" -> {
                // true && x => x
                if (left instanceof LiteralBoolean && ((LiteralBoolean) left).isBooleanTrue()) {
                    return right;
                }
                // x && true => x
                if (right instanceof LiteralBoolean && ((LiteralBoolean) right).isBooleanTrue()) {
                    return left;
                }
                // false && x => false
                if (left instanceof LiteralBoolean && !((LiteralBoolean) left).isBooleanTrue()) {
                    return new LiteralBoolean(false);
                }
                // x && false => false
                if (right instanceof LiteralBoolean && !((LiteralBoolean) right).isBooleanTrue()) {
                    return new LiteralBoolean(false);
                }
            }
            case "||" -> {
                // true || x => true
                if (left instanceof LiteralBoolean && ((LiteralBoolean) left).isBooleanTrue()) {
                    return new LiteralBoolean(true);
                }
                // x || true => true
                if (right instanceof LiteralBoolean && ((LiteralBoolean) right).isBooleanTrue()) {
                    return new LiteralBoolean(true);
                }
                // false || x => x
                if (left instanceof LiteralBoolean && !((LiteralBoolean) left).isBooleanTrue()) {
                    return right;
                }
                // x || false => x
                if (right instanceof LiteralBoolean && !((LiteralBoolean) right).isBooleanTrue()) {
                    return left;
                }
            }
            case "-->" -> {
                // false --> x => true
                if (left instanceof LiteralBoolean && !((LiteralBoolean) left).isBooleanTrue()) {
                    return new LiteralBoolean(true);
                }
                // x --> true => true
                if (right instanceof LiteralBoolean && ((LiteralBoolean) right).isBooleanTrue()) {
                    return new LiteralBoolean(true);
                }
                // true --> x => x
                if (left instanceof LiteralBoolean && ((LiteralBoolean) left).isBooleanTrue()) {
                    return right;
                }
            }
            // arithmetic simplifications
            case "+" -> {
                // 0 + x => x
                if (left instanceof LiteralInt && ((LiteralInt) left).getValue() == 0) {
                    return right;
                }
                // x + 0 => x
                if (right instanceof LiteralInt && ((LiteralInt) right).getValue() == 0) {
                    return left;
                }
                // 0.0 + x => x
                if (left instanceof LiteralReal && ((LiteralReal) left).getValue() == 0.0) {
                    return right;
                }
                // x + 0.0 => x
                if (right instanceof LiteralReal && ((LiteralReal) right).getValue() == 0.0) {
                    return left;
                }
            }
            case "-" -> {
                // x - 0 => x
                if (right instanceof LiteralInt && ((LiteralInt) right).getValue() == 0) {
                    return left;
                }
                // x - 0.0 => x
                if (right instanceof LiteralReal && ((LiteralReal) right).getValue() == 0.0) {
                    return left;
                }
            }
            case "*" -> {
                // 1 * x => x
                if (left instanceof LiteralInt && ((LiteralInt) left).getValue() == 1) {
                    return right;
                }
                // x * 1 => x
                if (right instanceof LiteralInt && ((LiteralInt) right).getValue() == 1) {
                    return left;
                }
                // 0 * x => 0
                if (left instanceof LiteralInt && ((LiteralInt) left).getValue() == 0) {
                    return new LiteralInt(0);
                }
                // x * 0 => 0
                if (right instanceof LiteralInt && ((LiteralInt) right).getValue() == 0) {
                    return new LiteralInt(0);
                }
                // 1.0 * x => x
                if (left instanceof LiteralReal && ((LiteralReal) left).getValue() == 1.0) {
                    return right;
                }
                // x * 1.0 => x
                if (right instanceof LiteralReal && ((LiteralReal) right).getValue() == 1.0) {
                    return left;
                }
                // 0.0 * x => 0.0
                if (left instanceof LiteralReal && ((LiteralReal) left).getValue() == 0.0) {
                    return new LiteralReal(0.0);
                }
                // x * 0.0 => 0.0
                if (right instanceof LiteralReal && ((LiteralReal) right).getValue() == 0.0) {
                    return new LiteralReal(0.0);
                }
            }
            case "/" -> {
                // x / 1 => x
                if (right instanceof LiteralInt && ((LiteralInt) right).getValue() == 1) {
                    return left;
                }
                // x / 1.0 => x
                if (right instanceof LiteralReal && ((LiteralReal) right).getValue() == 1.0) {
                    return left;
                }
                // 0 / x => 0 (assuming x != 0)
                if (left instanceof LiteralInt && ((LiteralInt) left).getValue() == 0) {
                    return new LiteralInt(0);
                }
                // 0.0 / x => 0.0 (assuming x != 0)
                if (left instanceof LiteralReal && ((LiteralReal) left).getValue() == 0.0) {
                    return new LiteralReal(0.0);
                }
            }
            // comparison simplifications
            case "==" -> {
                // x == x => true
                if (left.equals(right)) {
                    return new LiteralBoolean(true);
                }
            }
            case "!=" -> {
                // x != x => false
                if (left.equals(right)) {
                    return new LiteralBoolean(false);
                }
            }
        }
        // no simplification
        return binExp;
    }

    private static Expression simplifyUnaryExpression(UnaryExpression unaryExpr) {
        Expression operand = unaryExpr.getChildren().get(0);
        String operator = unaryExpr.getOp();
        switch (operator) {
            case "!" -> {
                // !!x => x (double negation)
                if (operand instanceof UnaryExpression) {
                    UnaryExpression innerUnary = (UnaryExpression) operand;
                    if (innerUnary.getOp().equals("!")) {
                        return innerUnary.getChildren().get(0);
                    }
                }
            }
            case "-" -> {
                // -(-x) => x (double negation)
                if (operand instanceof UnaryExpression) {
                    UnaryExpression innerUnary = (UnaryExpression) operand;
                    if (innerUnary.getOp().equals("-")) {
                        return innerUnary.getChildren().get(0);
                    }
                }
            }
        }
        return unaryExpr;
    }

    private static Expression simplifyIte(Ite iteExp) {
        Expression condExp = iteExp.getChildren().get(0);
        Expression thenExp = iteExp.getChildren().get(1);
        Expression elseExp = iteExp.getChildren().get(2);
        if (condExp instanceof LiteralBoolean) {
            boolean cond = ((LiteralBoolean) condExp).isBooleanTrue();
            return cond ? thenExp : elseExp;
        }
        return iteExp;
    }

    private static Expression simplifyGroupExpression(GroupExpression groupExp) {
        // (expression) => expression
        if (groupExp.getChildren().size() == 1) {
            return groupExp.getChildren().get(0);
        }
        return groupExp;
    }
}
