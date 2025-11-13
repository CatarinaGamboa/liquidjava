package liquidjava.rj_language.ast;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for Expression classes (Var, Literals, BinaryExpression, UnaryExpression, etc.)
 */
class ExpressionTest {

    @BeforeEach
    void setUp() {
        // Any setup needed
    }

    // =============== Var Tests ===============

    @Test
    void testVarConstructor() {
        Var var = new Var("x");
        assertNotNull(var, "Var should not be null");
        assertEquals("x", var.getName(), "Var name should be 'x'");
        assertEquals("x", var.toString(), "Var toString should be 'x'");
    }

    @Test
    void testVarEquals() {
        Var var1 = new Var("x");
        Var var2 = new Var("x");
        Var var3 = new Var("y");

        assertEquals(var1, var2, "Vars with same name should be equal");
        assertNotEquals(var1, var3, "Vars with different names should not be equal");
        assertEquals(var1.hashCode(), var2.hashCode(), "Equal vars should have same hashCode");
    }

    @Test
    void testVarClone() {
        Var var = new Var("x");
        Expression cloned = var.clone();

        assertNotNull(cloned, "Cloned var should not be null");
        assertTrue(cloned instanceof Var, "Cloned should be a Var");
        assertEquals(var.getName(), ((Var) cloned).getName(), "Cloned var should have same name");
        assertNotSame(var, cloned, "Cloned var should be different object");
    }

    @Test
    void testVarGetVariableNames() {
        Var var = new Var("myVar");
        List<String> names = new ArrayList<>();
        var.getVariableNames(names);

        assertEquals(1, names.size(), "Should have 1 variable name");
        assertEquals("myVar", names.get(0), "Variable name should be 'myVar'");
    }

    @Test
    void testVarIsBooleanTrue() {
        Var var = new Var("x");
        assertFalse(var.isBooleanTrue(), "Var should not be boolean true");
    }

    // =============== LiteralInt Tests ===============

    @Test
    void testLiteralIntWithString() {
        LiteralInt lit = new LiteralInt("42");
        assertNotNull(lit, "LiteralInt should not be null");
        assertEquals("42", lit.toString(), "LiteralInt toString should be '42'");
        assertEquals(42, lit.getValue(), "LiteralInt value should be 42");
    }

    @Test
    void testLiteralIntWithInt() {
        LiteralInt lit = new LiteralInt(100);
        assertNotNull(lit, "LiteralInt should not be null");
        assertEquals("100", lit.toString(), "LiteralInt toString should be '100'");
        assertEquals(100, lit.getValue(), "LiteralInt value should be 100");
    }

    @Test
    void testLiteralIntNegative() {
        LiteralInt lit = new LiteralInt(-50);
        assertEquals(-50, lit.getValue(), "LiteralInt value should be -50");
        assertEquals("-50", lit.toString(), "LiteralInt toString should be '-50'");
    }

    @Test
    void testLiteralIntEquals() {
        LiteralInt lit1 = new LiteralInt(42);
        LiteralInt lit2 = new LiteralInt(42);
        LiteralInt lit3 = new LiteralInt(100);

        assertEquals(lit1, lit2, "LiteralInts with same value should be equal");
        assertNotEquals(lit1, lit3, "LiteralInts with different values should not be equal");
        assertEquals(lit1.hashCode(), lit2.hashCode(), "Equal literals should have same hashCode");
    }

    @Test
    void testLiteralIntClone() {
        LiteralInt lit = new LiteralInt(42);
        Expression cloned = lit.clone();

        assertTrue(cloned instanceof LiteralInt, "Cloned should be LiteralInt");
        assertEquals(lit.getValue(), ((LiteralInt) cloned).getValue(), "Cloned should have same value");
    }

    @Test
    void testLiteralIntIsBooleanTrue() {
        LiteralInt lit = new LiteralInt(1);
        assertFalse(lit.isBooleanTrue(), "LiteralInt should not be boolean true");
    }

    @Test
    void testLiteralIntIsLiteral() {
        LiteralInt lit = new LiteralInt(42);
        assertTrue(lit.isLiteral(), "LiteralInt should be a literal");
    }

    // =============== LiteralBoolean Tests ===============

    @Test
    void testLiteralBooleanTrue() {
        LiteralBoolean lit = new LiteralBoolean(true);
        assertNotNull(lit, "LiteralBoolean should not be null");
        assertTrue(lit.isBooleanTrue(), "LiteralBoolean(true) should be true");
        assertEquals("true", lit.toString(), "LiteralBoolean toString should be 'true'");
    }

    @Test
    void testLiteralBooleanFalse() {
        LiteralBoolean lit = new LiteralBoolean(false);
        assertFalse(lit.isBooleanTrue(), "LiteralBoolean(false) should be false");
        assertEquals("false", lit.toString(), "LiteralBoolean toString should be 'false'");
    }

    @Test
    void testLiteralBooleanWithString() {
        LiteralBoolean litTrue = new LiteralBoolean("true");
        LiteralBoolean litFalse = new LiteralBoolean("false");

        assertTrue(litTrue.isBooleanTrue(), "LiteralBoolean('true') should be true");
        assertFalse(litFalse.isBooleanTrue(), "LiteralBoolean('false') should be false");
    }

    @Test
    void testLiteralBooleanEquals() {
        LiteralBoolean lit1 = new LiteralBoolean(true);
        LiteralBoolean lit2 = new LiteralBoolean(true);
        LiteralBoolean lit3 = new LiteralBoolean(false);

        assertEquals(lit1, lit2, "LiteralBooleans with same value should be equal");
        assertNotEquals(lit1, lit3, "LiteralBooleans with different values should not be equal");
    }

    @Test
    void testLiteralBooleanClone() {
        LiteralBoolean lit = new LiteralBoolean(true);
        Expression cloned = lit.clone();

        assertTrue(cloned instanceof LiteralBoolean, "Cloned should be LiteralBoolean");
        assertEquals(lit.isBooleanTrue(), ((LiteralBoolean) cloned).isBooleanTrue(), "Cloned should have same value");
    }

    @Test
    void testLiteralBooleanIsLiteral() {
        LiteralBoolean lit = new LiteralBoolean(true);
        assertTrue(lit.isLiteral(), "LiteralBoolean should be a literal");
    }

    @Test
    void testLiteralBooleanIsBooleanExpression() {
        LiteralBoolean lit = new LiteralBoolean(true);
        assertTrue(lit.isBooleanExpression(), "LiteralBoolean should be a boolean expression");
    }

    // =============== LiteralReal Tests ===============

    @Test
    void testLiteralRealWithString() {
        LiteralReal lit = new LiteralReal("3.14");
        assertNotNull(lit, "LiteralReal should not be null");
        assertEquals("3.14", lit.toString(), "LiteralReal toString should be '3.14'");
        assertEquals(3.14, lit.getValue(), 0.0001, "LiteralReal value should be 3.14");
    }

    @Test
    void testLiteralRealWithDouble() {
        LiteralReal lit = new LiteralReal(2.5);
        assertEquals(2.5, lit.getValue(), 0.0001, "LiteralReal value should be 2.5");
        assertEquals("2.5", lit.toString(), "LiteralReal toString should be '2.5'");
    }

    @Test
    void testLiteralRealEquals() {
        LiteralReal lit1 = new LiteralReal(3.14);
        LiteralReal lit2 = new LiteralReal(3.14);
        LiteralReal lit3 = new LiteralReal(2.5);

        assertEquals(lit1, lit2, "LiteralReals with same value should be equal");
        assertNotEquals(lit1, lit3, "LiteralReals with different values should not be equal");
    }

    @Test
    void testLiteralRealClone() {
        LiteralReal lit = new LiteralReal(3.14);
        Expression cloned = lit.clone();

        assertTrue(cloned instanceof LiteralReal, "Cloned should be LiteralReal");
        assertEquals(lit.getValue(), ((LiteralReal) cloned).getValue(), 0.0001, "Cloned should have same value");
    }

    @Test
    void testLiteralRealIsLiteral() {
        LiteralReal lit = new LiteralReal(3.14);
        assertTrue(lit.isLiteral(), "LiteralReal should be a literal");
    }

    // =============== BinaryExpression Tests ===============

    @Test
    void testBinaryExpressionConstructor() {
        Expression left = new Var("x");
        Expression right = new LiteralInt(5);
        BinaryExpression binExpr = new BinaryExpression(left, "+", right);

        assertNotNull(binExpr, "BinaryExpression should not be null");
        assertEquals(left, binExpr.getLeft(), "Left operand should match");
        assertEquals(right, binExpr.getRight(), "Right operand should match");
        assertEquals("+", binExpr.getOp(), "Operator should be '+'");
    }

    @Test
    void testBinaryExpressionToString() {
        Expression left = new Var("x");
        Expression right = new LiteralInt(5);
        BinaryExpression binExpr = new BinaryExpression(left, "+", right);

        String str = binExpr.toString();
        assertTrue(str.contains("x"), "toString should contain 'x'");
        assertTrue(str.contains("+"), "toString should contain '+'");
        assertTrue(str.contains("5"), "toString should contain '5'");
    }

    @Test
    void testBinaryExpressionArithmetic() {
        BinaryExpression add = new BinaryExpression(new LiteralInt(2), "+", new LiteralInt(3));
        BinaryExpression sub = new BinaryExpression(new LiteralInt(5), "-", new LiteralInt(3));
        BinaryExpression mul = new BinaryExpression(new LiteralInt(4), "*", new LiteralInt(3));
        BinaryExpression div = new BinaryExpression(new LiteralInt(10), "/", new LiteralInt(2));

        assertEquals("+", add.getOp(), "Add operator should be '+'");
        assertEquals("-", sub.getOp(), "Sub operator should be '-'");
        assertEquals("*", mul.getOp(), "Mul operator should be '*'");
        assertEquals("/", div.getOp(), "Div operator should be '/'");
    }

    @Test
    void testBinaryExpressionComparison() {
        BinaryExpression eq = new BinaryExpression(new Var("x"), "==", new LiteralInt(5));
        BinaryExpression neq = new BinaryExpression(new Var("x"), "!=", new LiteralInt(5));
        BinaryExpression lt = new BinaryExpression(new Var("x"), "<", new LiteralInt(5));
        BinaryExpression gt = new BinaryExpression(new Var("x"), ">", new LiteralInt(5));
        BinaryExpression lte = new BinaryExpression(new Var("x"), "<=", new LiteralInt(5));
        BinaryExpression gte = new BinaryExpression(new Var("x"), ">=", new LiteralInt(5));

        assertEquals("==", eq.getOp(), "Operator should be '=='");
        assertEquals("!=", neq.getOp(), "Operator should be '!='");
        assertEquals("<", lt.getOp(), "Operator should be '<'");
        assertEquals(">", gt.getOp(), "Operator should be '>'");
        assertEquals("<=", lte.getOp(), "Operator should be '<='");
        assertEquals(">=", gte.getOp(), "Operator should be '>='");
    }

    @Test
    void testBinaryExpressionLogical() {
        BinaryExpression and = new BinaryExpression(new LiteralBoolean(true), "&&", new LiteralBoolean(false));
        BinaryExpression or = new BinaryExpression(new LiteralBoolean(true), "||", new LiteralBoolean(false));

        assertEquals("&&", and.getOp(), "Operator should be '&&'");
        assertEquals("||", or.getOp(), "Operator should be '||'");
    }

    @Test
    void testBinaryExpressionClone() {
        BinaryExpression binExpr = new BinaryExpression(new Var("x"), "+", new LiteralInt(5));
        Expression cloned = binExpr.clone();

        assertTrue(cloned instanceof BinaryExpression, "Cloned should be BinaryExpression");
        BinaryExpression clonedBin = (BinaryExpression) cloned;
        assertEquals(binExpr.getOp(), clonedBin.getOp(), "Operator should match");
    }

    @Test
    void testBinaryExpressionGetVariableNames() {
        BinaryExpression binExpr = new BinaryExpression(new Var("x"), "+", new Var("y"));
        List<String> names = new ArrayList<>();
        binExpr.getVariableNames(names);

        assertEquals(2, names.size(), "Should have 2 variables");
        assertTrue(names.contains("x"), "Should contain 'x'");
        assertTrue(names.contains("y"), "Should contain 'y'");
    }

    @Test
    void testBinaryExpressionIsBooleanExpression() {
        BinaryExpression comparison = new BinaryExpression(new Var("x"), "==", new LiteralInt(5));
        BinaryExpression logical = new BinaryExpression(new LiteralBoolean(true), "&&", new LiteralBoolean(false));
        BinaryExpression arithmetic = new BinaryExpression(new LiteralInt(2), "+", new LiteralInt(3));

        assertTrue(comparison.isBooleanExpression(), "Comparison should be boolean expression");
        assertTrue(logical.isBooleanExpression(), "Logical operation should be boolean expression");
        assertFalse(arithmetic.isBooleanExpression(), "Arithmetic should not be boolean expression");
    }

    // =============== UnaryExpression Tests ===============

    @Test
    void testUnaryExpressionConstructor() {
        Expression operand = new Var("x");
        UnaryExpression unaryExpr = new UnaryExpression("-", operand);

        assertNotNull(unaryExpr, "UnaryExpression should not be null");
        assertEquals("-", unaryExpr.getOp(), "Operator should be '-'");
        assertEquals(operand, unaryExpr.getOperand(), "Operand should match");
    }

    @Test
    void testUnaryExpressionNegation() {
        UnaryExpression negation = new UnaryExpression("-", new LiteralInt(5));
        assertEquals("-", negation.getOp(), "Operator should be '-'");
        assertTrue(negation.toString().contains("-"), "toString should contain '-'");
    }

    @Test
    void testUnaryExpressionNot() {
        UnaryExpression not = new UnaryExpression("!", new LiteralBoolean(true));
        assertEquals("!", not.getOp(), "Operator should be '!'");
        assertTrue(not.isBooleanExpression(), "NOT operation should be boolean expression");
    }

    @Test
    void testUnaryExpressionClone() {
        UnaryExpression unaryExpr = new UnaryExpression("-", new Var("x"));
        Expression cloned = unaryExpr.clone();

        assertTrue(cloned instanceof UnaryExpression, "Cloned should be UnaryExpression");
        assertEquals(unaryExpr.getOp(), ((UnaryExpression) cloned).getOp(), "Operator should match");
    }

    @Test
    void testUnaryExpressionGetVariableNames() {
        UnaryExpression unaryExpr = new UnaryExpression("-", new Var("x"));
        List<String> names = new ArrayList<>();
        unaryExpr.getVariableNames(names);

        assertEquals(1, names.size(), "Should have 1 variable");
        assertEquals("x", names.get(0), "Variable should be 'x'");
    }

    // =============== GroupExpression Tests ===============

    @Test
    void testGroupExpressionConstructor() {
        Expression inner = new Var("x");
        GroupExpression groupExpr = new GroupExpression(inner);

        assertNotNull(groupExpr, "GroupExpression should not be null");
        assertEquals(inner, groupExpr.getExpression(), "Inner expression should match");
    }

    @Test
    void testGroupExpressionToString() {
        GroupExpression groupExpr = new GroupExpression(new Var("x"));
        String str = groupExpr.toString();

        assertTrue(str.contains("(") && str.contains(")"), "toString should contain parentheses");
        assertTrue(str.contains("x"), "toString should contain 'x'");
    }

    @Test
    void testGroupExpressionClone() {
        GroupExpression groupExpr = new GroupExpression(new Var("x"));
        Expression cloned = groupExpr.clone();

        assertTrue(cloned instanceof GroupExpression, "Cloned should be GroupExpression");
    }

    @Test
    void testGroupExpressionIsBooleanExpression() {
        GroupExpression boolGroup = new GroupExpression(new LiteralBoolean(true));
        GroupExpression intGroup = new GroupExpression(new LiteralInt(5));

        assertTrue(boolGroup.isBooleanExpression(), "Group with boolean should be boolean expression");
        assertFalse(intGroup.isBooleanExpression(), "Group with int should not be boolean expression");
    }

    // =============== Ite (If-Then-Else) Tests ===============

    @Test
    void testIteConstructor() {
        Expression condition = new Var("cond");
        Expression thenBranch = new LiteralInt(1);
        Expression elseBranch = new LiteralInt(0);
        Ite ite = new Ite(condition, thenBranch, elseBranch);

        assertNotNull(ite, "Ite should not be null");
        assertEquals(condition, ite.getCondition(), "Condition should match");
        assertEquals(thenBranch, ite.getThen(), "Then branch should match");
        assertEquals(elseBranch, ite.getElse(), "Else branch should match");
    }

    @Test
    void testIteToString() {
        Ite ite = new Ite(new Var("cond"), new LiteralInt(1), new LiteralInt(0));
        String str = ite.toString();

        assertTrue(str.contains("cond"), "toString should contain condition");
        assertTrue(str.contains("1"), "toString should contain then branch");
        assertTrue(str.contains("0"), "toString should contain else branch");
    }

    @Test
    void testIteClone() {
        Ite ite = new Ite(new Var("cond"), new LiteralInt(1), new LiteralInt(0));
        Expression cloned = ite.clone();

        assertTrue(cloned instanceof Ite, "Cloned should be Ite");
    }

    @Test
    void testIteIsBooleanExpression() {
        Ite ite = new Ite(new Var("cond"), new LiteralInt(1), new LiteralInt(0));
        assertTrue(ite.isBooleanExpression(), "Ite should be boolean expression");
    }

    @Test
    void testIteGetVariableNames() {
        Ite ite = new Ite(new Var("cond"), new Var("x"), new Var("y"));
        List<String> names = new ArrayList<>();
        ite.getVariableNames(names);

        assertEquals(3, names.size(), "Should have 3 variables");
        assertTrue(names.contains("cond"), "Should contain 'cond'");
        assertTrue(names.contains("x"), "Should contain 'x'");
        assertTrue(names.contains("y"), "Should contain 'y'");
    }

    // =============== FunctionInvocation Tests ===============

    @Test
    void testFunctionInvocationConstructor() {
        List<Expression> args = new ArrayList<>();
        args.add(new Var("x"));
        args.add(new LiteralInt(5));
        FunctionInvocation func = new FunctionInvocation("myFunc", args);

        assertNotNull(func, "FunctionInvocation should not be null");
        assertEquals("myFunc", func.getName(), "Function name should be 'myFunc'");
        assertEquals(2, func.getArgs().size(), "Should have 2 arguments");
    }

    @Test
    void testFunctionInvocationToString() {
        List<Expression> args = new ArrayList<>();
        args.add(new Var("x"));
        FunctionInvocation func = new FunctionInvocation("myFunc", args);

        String str = func.toString();
        assertTrue(str.contains("myFunc"), "toString should contain function name");
        assertTrue(str.contains("x"), "toString should contain argument");
    }

    @Test
    void testFunctionInvocationClone() {
        List<Expression> args = new ArrayList<>();
        args.add(new Var("x"));
        FunctionInvocation func = new FunctionInvocation("myFunc", args);
        Expression cloned = func.clone();

        assertTrue(cloned instanceof FunctionInvocation, "Cloned should be FunctionInvocation");
        assertEquals(func.getName(), ((FunctionInvocation) cloned).getName(), "Name should match");
    }

    @Test
    void testFunctionInvocationGetVariableNames() {
        List<Expression> args = new ArrayList<>();
        args.add(new Var("x"));
        args.add(new Var("y"));
        FunctionInvocation func = new FunctionInvocation("myFunc", args);

        List<String> names = new ArrayList<>();
        func.getVariableNames(names);

        assertEquals(2, names.size(), "Should have 2 variables");
        assertTrue(names.contains("x"), "Should contain 'x'");
        assertTrue(names.contains("y"), "Should contain 'y'");
    }

    @Test
    void testFunctionInvocationIsBooleanExpression() {
        List<Expression> args = new ArrayList<>();
        FunctionInvocation func = new FunctionInvocation("myFunc", args);
        assertTrue(func.isBooleanExpression(), "FunctionInvocation should be boolean expression");
    }

    // =============== Expression Substitution Tests ===============

    @Test
    void testSubstituteSimple() {
        Var x = new Var("x");
        Var y = new Var("y");

        Expression result = x.substitute(x, y);
        assertTrue(result instanceof Var, "Result should be Var");
        assertEquals("y", ((Var) result).getName(), "Variable should be substituted");
    }

    @Test
    void testSubstituteInBinaryExpression() {
        Expression expr = new BinaryExpression(new Var("x"), "+", new LiteralInt(5));
        Expression result = expr.substitute(new Var("x"), new Var("y"));

        assertTrue(result instanceof BinaryExpression, "Result should be BinaryExpression");
        String str = result.toString();
        assertTrue(str.contains("y"), "Result should contain 'y'");
        assertFalse(str.contains("x"), "Result should not contain 'x'");
    }

    @Test
    void testSubstituteInComplexExpression() {
        // (x + y) * z
        Expression xy = new BinaryExpression(new Var("x"), "+", new Var("y"));
        Expression expr = new BinaryExpression(xy, "*", new Var("z"));

        // Substitute x with a
        Expression result = expr.substitute(new Var("x"), new Var("a"));
        String str = result.toString();

        assertTrue(str.contains("a"), "Result should contain 'a'");
        assertTrue(str.contains("y"), "Result should still contain 'y'");
        assertTrue(str.contains("z"), "Result should still contain 'z'");
    }
}
