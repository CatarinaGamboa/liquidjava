package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.Expression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the AliasWrapper class
 */
class AliasWrapperTest {

    private Factory factory;
    private Context context;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
        context = Context.getInstance();
        context.reinitializeAllContext();
    }

    @Test
    void testConstructorWithBasicParameters() {
        Predicate pred = Predicate.createVar("x");
        List<String> varNames = List.of("arg1");
        List<String> varTypes = List.of("int");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);

        assertNotNull(alias, "AliasWrapper should not be null");
        assertEquals("myAlias", alias.getName(), "Name should be 'myAlias'");
    }

    @Test
    void testGetName() {
        Predicate pred = new Predicate();
        List<String> varNames = List.of();
        List<String> varTypes = List.of();

        AliasWrapper alias = new AliasWrapper("testAlias", pred, varNames, varTypes);
        assertEquals("testAlias", alias.getName(), "Name should be 'testAlias'");
    }

    @Test
    void testGetVarNames() {
        Predicate pred = new Predicate();
        List<String> varNames = List.of("x", "y", "z");
        List<String> varTypes = List.of("int", "int", "int");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        List<String> retrievedNames = alias.getVarNames();

        assertEquals(3, retrievedNames.size(), "Should have 3 variable names");
        assertEquals("x", retrievedNames.get(0), "First name should be 'x'");
        assertEquals("y", retrievedNames.get(1), "Second name should be 'y'");
        assertEquals("z", retrievedNames.get(2), "Third name should be 'z'");
    }

    @Test
    void testGetTypes() {
        Predicate pred = new Predicate();
        List<String> varNames = List.of("x");
        List<String> varTypes = List.of("int");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        List<CtTypeReference<?>> types = alias.getTypes();

        assertNotNull(types, "Types should not be null");
        assertEquals(1, types.size(), "Should have 1 type");
    }

    @Test
    void testGetClonedPredicate() {
        Predicate pred = Predicate.createVar("x");
        List<String> varNames = List.of();
        List<String> varTypes = List.of();

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        Predicate cloned = alias.getClonedPredicate();

        assertNotNull(cloned, "Cloned predicate should not be null");
        assertEquals(pred.toString(), cloned.toString(), "Cloned predicate should match original");
    }

    @Test
    void testGetNewExpression() {
        Predicate pred = Predicate.createVar("x");
        List<String> varNames = List.of("x");
        List<String> varTypes = List.of("int");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        List<String> newNames = List.of("y");
        Expression newExpr = alias.getNewExpression(newNames);

        assertNotNull(newExpr, "New expression should not be null");
        assertEquals("y", newExpr.toString(), "Expression should have substituted variable");
    }

    @Test
    void testGetNewVariables() {
        Predicate pred = new Predicate();
        List<String> varNames = List.of("x", "y");
        List<String> varTypes = List.of("int", "int");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        List<String> newVars = alias.getNewVariables(context);

        assertEquals(2, newVars.size(), "Should have 2 new variables");
        assertTrue(newVars.get(0).contains("x"), "First variable should contain 'x'");
        assertTrue(newVars.get(1).contains("y"), "Second variable should contain 'y'");
    }

    @Test
    void testGetTypesWithNames() {
        Predicate pred = new Predicate();
        List<String> varNames = List.of("x", "y");
        List<String> varTypes = List.of("int", "String");

        AliasWrapper alias = new AliasWrapper("myAlias", pred, varNames, varTypes);
        List<String> newNames = List.of("a", "b");
        Map<String, CtTypeReference<?>> typesMap = alias.getTypes(newNames);

        assertEquals(2, typesMap.size(), "Map should have 2 entries");
        assertTrue(typesMap.containsKey("a"), "Map should contain 'a'");
        assertTrue(typesMap.containsKey("b"), "Map should contain 'b'");
    }

    @Test
    void testWithNoVariables() {
        Predicate pred = Predicate.createLit("true", "boolean");
        List<String> varNames = List.of();
        List<String> varTypes = List.of();

        AliasWrapper alias = new AliasWrapper("constantAlias", pred, varNames, varTypes);

        assertEquals(0, alias.getVarNames().size(), "Should have no variables");
        assertEquals(0, alias.getTypes().size(), "Should have no types");
    }

    @Test
    void testWithMultipleVariables() {
        Predicate pred = Predicate.createVar("x");
        List<String> varNames = List.of("x", "y", "z");
        List<String> varTypes = List.of("int", "double", "boolean");

        AliasWrapper alias = new AliasWrapper("multiVarAlias", pred, varNames, varTypes);

        assertEquals(3, alias.getVarNames().size(), "Should have 3 variables");
        assertEquals(3, alias.getTypes().size(), "Should have 3 types");
    }

    @Test
    void testSubstitution() {
        // Create predicate: x + y
        Predicate x = Predicate.createVar("x");
        Predicate y = Predicate.createVar("y");
        Predicate pred = Predicate.createOperation(x, "+", y);

        List<String> varNames = List.of("x", "y");
        List<String> varTypes = List.of("int", "int");

        AliasWrapper alias = new AliasWrapper("addAlias", pred, varNames, varTypes);
        List<String> newNames = List.of("a", "b");
        Expression newExpr = alias.getNewExpression(newNames);

        String exprStr = newExpr.toString();
        assertTrue(exprStr.contains("a"), "Expression should contain 'a'");
        assertTrue(exprStr.contains("b"), "Expression should contain 'b'");
        assertFalse(exprStr.contains("x"), "Expression should not contain 'x'");
        assertFalse(exprStr.contains("y"), "Expression should not contain 'y'");
    }
}
