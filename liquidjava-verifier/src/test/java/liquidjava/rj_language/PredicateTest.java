package liquidjava.rj_language;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import liquidjava.diagnostics.ErrorEmitter;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.BinaryExpression;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.ast.LiteralBoolean;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.Var;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import liquidjava.rj_language.parsing.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;

/**
 * Test suite for the Predicate class
 */
class PredicateTest {

    private Factory factory;
    private CtClass<?> testClass;
    private ErrorEmitter errorEmitter;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/test/java");
        launcher.buildModel();
        factory = launcher.getFactory();

        // Create a test class for context
        testClass = factory.Class().create("TestClass");
        errorEmitter = new ErrorEmitter();

        // Reset context
        Context.getInstance().reinitializeAllContext();
    }

    @Test
    void testDefaultConstructor() {
        Predicate pred = new Predicate();
        assertNotNull(pred, "Predicate should not be null");
        assertTrue(pred.isBooleanTrue(), "Default predicate should be true");
        assertEquals("true", pred.toString(), "Default predicate should have expression 'true'");
    }

    @Test
    void testConstructorWithExpression() {
        Expression expr = new LiteralBoolean(false);
        Predicate pred = new Predicate(expr);

        assertNotNull(pred, "Predicate should not be null");
        assertFalse(pred.isBooleanTrue(), "Predicate should be false");
        assertEquals(expr, pred.getExpression(), "Expression should match");
    }

    @Test
    void testIsBooleanTrue() {
        Predicate truePred = new Predicate(new LiteralBoolean(true));
        Predicate falsePred = new Predicate(new LiteralBoolean(false));

        assertTrue(truePred.isBooleanTrue(), "Predicate with true should return true");
        assertFalse(falsePred.isBooleanTrue(), "Predicate with false should return false");
    }

    @Test
    void testGetExpression() {
        Expression expr = new LiteralInt(42);
        Predicate pred = new Predicate(expr);

        Expression retrieved = pred.getExpression();
        assertEquals(expr, retrieved, "Retrieved expression should match");
    }

    @Test
    void testClone() {
        Expression expr = new Var("x");
        Predicate pred = new Predicate(expr);

        Predicate cloned = pred.clone();
        assertNotNull(cloned, "Cloned predicate should not be null");
        assertEquals(pred.toString(), cloned.toString(), "Cloned predicate should have same string representation");
        assertNotSame(pred, cloned, "Cloned predicate should be a different object");
    }

    @Test
    void testNegate() {
        Predicate pred = new Predicate(new Var("x"));
        Predicate negated = pred.negate();

        assertNotNull(negated, "Negated predicate should not be null");
        assertTrue(negated.toString().contains("!"), "Negated predicate should contain '!'");
    }

    @Test
    void testSubstituteVariable() {
        Predicate pred = Predicate.createVar("x");
        Predicate substituted = pred.substituteVariable("x", "y");

        assertEquals("y", substituted.toString(), "Variable x should be substituted with y");
    }

    @Test
    void testGetVariableNames() {
        // Create predicate: x + y
        Predicate pred = Predicate.createOperation(
                Predicate.createVar("x"),
                "+",
                Predicate.createVar("y")
        );

        List<String> varNames = pred.getVariableNames();
        assertEquals(2, varNames.size(), "Should have 2 variables");
        assertTrue(varNames.contains("x"), "Should contain variable x");
        assertTrue(varNames.contains("y"), "Should contain variable y");
    }

    @Test
    void testCreateConjunction() {
        Predicate p1 = Predicate.createVar("x");
        Predicate p2 = Predicate.createVar("y");
        Predicate conjunction = Predicate.createConjunction(p1, p2);

        assertNotNull(conjunction, "Conjunction should not be null");
        assertTrue(conjunction.toString().contains("&&"), "Conjunction should contain '&&'");
    }

    @Test
    void testCreateDisjunction() {
        Predicate p1 = Predicate.createVar("x");
        Predicate p2 = Predicate.createVar("y");
        Predicate disjunction = Predicate.createDisjunction(p1, p2);

        assertNotNull(disjunction, "Disjunction should not be null");
        assertTrue(disjunction.toString().contains("||"), "Disjunction should contain '||'");
    }

    @Test
    void testCreateEquals() {
        Predicate p1 = Predicate.createVar("x");
        Predicate p2 = Predicate.createLit("5", "int");
        Predicate equals = Predicate.createEquals(p1, p2);

        assertNotNull(equals, "Equals predicate should not be null");
        assertTrue(equals.toString().contains("=="), "Equals predicate should contain '=='");
    }

    @Test
    void testCreateITE() {
        Predicate cond = Predicate.createVar("condition");
        Predicate thenBranch = Predicate.createLit("true", "boolean");
        Predicate elseBranch = Predicate.createLit("false", "boolean");

        Predicate ite = Predicate.createITE(cond, thenBranch, elseBranch);
        assertNotNull(ite, "ITE predicate should not be null");
    }

    @Test
    void testCreateLitBoolean() {
        Predicate trueLit = Predicate.createLit("true", "boolean");
        Predicate falseLit = Predicate.createLit("false", "boolean");

        assertTrue(trueLit.isBooleanTrue(), "True literal should be true");
        assertFalse(falseLit.isBooleanTrue(), "False literal should be false");
    }

    @Test
    void testCreateLitInt() {
        Predicate intLit = Predicate.createLit("42", "int");
        assertNotNull(intLit, "Int literal should not be null");
        assertEquals("42", intLit.toString(), "Int literal should be '42'");
    }

    @Test
    void testCreateLitShort() {
        Predicate shortLit = Predicate.createLit("10", "short");
        assertNotNull(shortLit, "Short literal should not be null");
        assertEquals("10", shortLit.toString(), "Short literal should be '10'");
    }

    @Test
    void testCreateLitDouble() {
        Predicate doubleLit = Predicate.createLit("3.14", "double");
        assertNotNull(doubleLit, "Double literal should not be null");
        assertEquals("3.14", doubleLit.toString(), "Double literal should be '3.14'");
    }

    @Test
    void testCreateLitFloat() {
        Predicate floatLit = Predicate.createLit("2.5", "float");
        assertNotNull(floatLit, "Float literal should not be null");
        assertEquals("2.5", floatLit.toString(), "Float literal should be '2.5'");
    }

    @Test
    void testCreateLitLong() {
        Predicate longLit = Predicate.createLit("1000000", "long");
        assertNotNull(longLit, "Long literal should not be null");
        assertEquals("1000000", longLit.toString(), "Long literal should be '1000000'");
    }

    @Test
    void testCreateLitUnsupportedType() {
        assertThrows(IllegalArgumentException.class, () -> {
            Predicate.createLit("value", "unsupported");
        }, "Creating literal with unsupported type should throw exception");
    }

    @Test
    void testCreateVar() {
        Predicate varPred = Predicate.createVar("myVar");
        assertNotNull(varPred, "Variable predicate should not be null");
        assertEquals("myVar", varPred.toString(), "Variable predicate should be 'myVar'");
    }

    @Test
    void testCreateOperation() {
        Predicate left = Predicate.createLit("5", "int");
        Predicate right = Predicate.createLit("3", "int");
        Predicate operation = Predicate.createOperation(left, "+", right);

        assertNotNull(operation, "Operation should not be null");
        assertTrue(operation.toString().contains("+"), "Operation should contain '+'");
    }

    @Test
    void testCreateInvocation() {
        Predicate arg1 = Predicate.createVar("x");
        Predicate arg2 = Predicate.createVar("y");
        Predicate invocation = Predicate.createInvocation("myFunc", arg1, arg2);

        assertNotNull(invocation, "Invocation should not be null");
        assertTrue(invocation.toString().contains("myFunc"), "Invocation should contain function name");
    }

    @Test
    void testSimplify() {
        // Create a simple expression that can be simplified: 2 + 3
        Predicate pred = Predicate.createOperation(
                Predicate.createLit("2", "int"),
                "+",
                Predicate.createLit("3", "int")
        );

        ValDerivationNode result = pred.simplify();
        assertNotNull(result, "Simplification result should not be null");
        assertNotNull(result.getValue(), "Simplified value should not be null");
    }

    @Test
    void testToString() {
        Predicate pred = Predicate.createVar("x");
        String str = pred.toString();

        assertNotNull(str, "toString should not return null");
        assertEquals("x", str, "toString should return 'x'");
    }

    @Test
    void testComplexPredicate() {
        // Create: (x > 5) && (y < 10)
        Predicate x = Predicate.createVar("x");
        Predicate five = Predicate.createLit("5", "int");
        Predicate xGreater5 = Predicate.createOperation(x, ">", five);

        Predicate y = Predicate.createVar("y");
        Predicate ten = Predicate.createLit("10", "int");
        Predicate yLess10 = Predicate.createOperation(y, "<", ten);

        Predicate complex = Predicate.createConjunction(xGreater5, yLess10);

        assertNotNull(complex, "Complex predicate should not be null");
        String str = complex.toString();
        assertTrue(str.contains("x") && str.contains("y"), "Complex predicate should contain both variables");
        assertTrue(str.contains(">") && str.contains("<"), "Complex predicate should contain both operators");
        assertTrue(str.contains("&&"), "Complex predicate should contain conjunction");
    }

    @Test
    void testGetOldVariableNames() {
        // Create predicate with old() function
        Predicate oldX = Predicate.createInvocation("old", Predicate.createVar("x"));
        List<String> oldVars = oldX.getOldVariableNames();

        assertEquals(1, oldVars.size(), "Should have 1 old variable");
        assertTrue(oldVars.contains("x"), "Should contain variable x");
    }

    @Test
    void testChangeOldMentions() {
        // Create predicate with old(x) and change it to y
        Predicate oldX = Predicate.createInvocation("old", Predicate.createVar("x"));
        Predicate changed = oldX.changeOldMentions("x", "y", errorEmitter);

        assertNotNull(changed, "Changed predicate should not be null");
        assertEquals("y", changed.toString(), "old(x) should be changed to y");
    }

    @Test
    void testMultipleVariables() {
        // Create: x + y + z
        Predicate x = Predicate.createVar("x");
        Predicate y = Predicate.createVar("y");
        Predicate z = Predicate.createVar("z");

        Predicate xy = Predicate.createOperation(x, "+", y);
        Predicate xyz = Predicate.createOperation(xy, "+", z);

        List<String> varNames = xyz.getVariableNames();
        assertEquals(3, varNames.size(), "Should have 3 variables");
        assertTrue(varNames.contains("x"), "Should contain x");
        assertTrue(varNames.contains("y"), "Should contain y");
        assertTrue(varNames.contains("z"), "Should contain z");
    }
}
