package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.LiteralBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the Context class, which manages the global state for verification
 */
class ContextTest {

    private Context context;
    private Factory factory;

    @BeforeEach
    void setUp() {
        // Get Context singleton instance
        context = Context.getInstance();
        // Reinitialize to ensure clean state for each test
        context.reinitializeAllContext();

        // Create a Spoon factory for creating type references
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
    }

    @Test
    void testGetInstance() {
        Context ctx1 = Context.getInstance();
        Context ctx2 = Context.getInstance();
        assertSame(ctx1, ctx2, "Context should be a singleton");
    }

    @Test
    void testReinitializeContext() {
        // Add a variable
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();
        context.addVarToContext("x", intType, pred, null);

        assertTrue(context.hasVariable("x"), "Variable x should exist");

        // Reinitialize
        context.reinitializeContext();

        assertFalse(context.hasVariable("x"), "Variable x should be removed after reinitialization");
    }

    @Test
    void testReinitializeAllContext() {
        // Add various elements
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);
        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        context.addFunctionToContext(func);

        int counterBefore = context.getCounter();
        assertTrue(counterBefore >= 0, "Counter should be non-negative");

        // Reinitialize all
        context.reinitializeAllContext();

        assertFalse(context.hasVariable("x"), "Variables should be cleared");
        assertEquals(0, context.counter, "Counter should be reset to 0");
    }

    @Test
    void testEnterAndExitContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        // Add a variable in the global scope
        context.addVarToContext("x", intType, pred, null);
        assertTrue(context.hasVariable("x"), "Variable x should exist in global scope");

        // Enter a new context
        context.enterContext();

        // Add a variable in the new scope
        context.addVarToContext("y", intType, pred, null);
        assertTrue(context.hasVariable("y"), "Variable y should exist in inner scope");
        assertTrue(context.hasVariable("x"), "Variable x should still be accessible");

        // Exit the context
        context.exitContext();

        assertTrue(context.hasVariable("x"), "Variable x should still exist after exiting scope");
        assertFalse(context.hasVariable("y"), "Variable y should not exist after exiting scope");
    }

    @Test
    void testGetCounter() {
        int counter1 = context.getCounter();
        int counter2 = context.getCounter();
        int counter3 = context.getCounter();

        assertEquals(counter1 + 1, counter2, "Counter should increment by 1");
        assertEquals(counter2 + 1, counter3, "Counter should continue incrementing");
    }

    @Test
    void testAddVarToContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        assertFalse(context.hasVariable("x"), "Variable x should not exist initially");

        RefinedVariable var = context.addVarToContext("x", intType, pred, null);

        assertNotNull(var, "Returned variable should not be null");
        assertTrue(context.hasVariable("x"), "Variable x should exist after adding");
        assertEquals("x", var.getName(), "Variable name should be 'x'");
        assertEquals(intType, var.getType(), "Variable type should match");
    }

    @Test
    void testAddGlobalVariableToContext() {
        CtTypeReference<String> stringType = factory.Type().stringType();
        Predicate pred = new Predicate();

        context.addGlobalVariableToContext("globalVar", stringType, pred);

        assertTrue(context.hasVariable("globalVar"), "Global variable should exist");

        RefinedVariable var = context.getVariableByName("globalVar");
        assertNotNull(var, "Retrieved variable should not be null");
        assertEquals("globalVar", var.getName(), "Variable name should match");
    }

    @Test
    void testGetVariableByName() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);

        RefinedVariable var = context.getVariableByName("x");
        assertNotNull(var, "Variable should be found");
        assertEquals("x", var.getName(), "Variable name should be 'x'");

        RefinedVariable notFound = context.getVariableByName("nonexistent");
        assertNull(notFound, "Nonexistent variable should return null");
    }

    @Test
    void testHasVariable() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        assertFalse(context.hasVariable("x"), "Variable x should not exist initially");

        context.addVarToContext("x", intType, pred, null);

        assertTrue(context.hasVariable("x"), "Variable x should exist after adding");
        assertFalse(context.hasVariable("y"), "Variable y should not exist");
    }

    @Test
    void testGetAllVariables() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);
        context.addVarToContext("y", intType, pred, null);

        List<RefinedVariable> vars = context.getAllVariables();

        assertEquals(2, vars.size(), "Should have 2 variables");
        assertTrue(vars.stream().anyMatch(v -> v.getName().equals("x")), "List should contain variable x");
        assertTrue(vars.stream().anyMatch(v -> v.getName().equals("y")), "List should contain variable y");
    }

    @Test
    void testNewRefinementToVariableInContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred1 = new Predicate(new LiteralBoolean(true));
        Predicate pred2 = new Predicate(new LiteralBoolean(false));

        context.addVarToContext("x", intType, pred1, null);

        RefinedVariable var = context.getVariableByName("x");
        assertEquals(pred1.toString(), var.getRefinement().toString(), "Initial refinement should match");

        context.newRefinementToVariableInContext("x", pred2);

        var = context.getVariableByName("x");
        assertEquals(pred2.toString(), var.getRefinement().toString(), "Refinement should be updated");
    }

    @Test
    void testGetVariableRefinements() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);

        Predicate refinement = context.getVariableRefinements("x");
        assertNotNull(refinement, "Refinement should not be null");

        Predicate nonexistent = context.getVariableRefinements("nonexistent");
        assertNull(nonexistent, "Refinement for nonexistent variable should be null");
    }

    @Test
    void testGetContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        CtTypeReference<String> stringType = factory.Type().stringType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);
        context.addVarToContext("y", stringType, pred, null);

        Map<String, CtTypeReference<?>> contextMap = context.getContext();

        assertNotNull(contextMap, "Context map should not be null");
        assertEquals(2, contextMap.size(), "Context map should have 2 entries");
        assertEquals(intType, contextMap.get("x"), "Type for x should match");
        assertEquals(stringType, contextMap.get("y"), "Type for y should match");
    }

    @Test
    void testAddFunctionToContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        context.addFunctionToContext(func);

        RefinedFunction retrieved = context.getFunction("testFunc", "TestClass");
        assertNotNull(retrieved, "Function should be found");
        assertEquals("testFunc", retrieved.getName(), "Function name should match");
        assertEquals("TestClass", retrieved.getTargetClass(), "Target class should match");
    }

    @Test
    void testGetFunction() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        context.addFunctionToContext(func);

        RefinedFunction retrieved = context.getFunction("testFunc", "TestClass");
        assertNotNull(retrieved, "Function should be found");

        RefinedFunction notFound = context.getFunction("nonexistent", "TestClass");
        assertNull(notFound, "Nonexistent function should return null");
    }

    @Test
    void testGetFunctionWithSize() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        func.addArgument("arg1", intType);
        func.addArgument("arg2", intType);
        context.addFunctionToContext(func);

        RefinedFunction retrieved = context.getFunction("testFunc", "TestClass", 2);
        assertNotNull(retrieved, "Function with 2 arguments should be found");

        RefinedFunction notFound = context.getFunction("testFunc", "TestClass", 1);
        assertNull(notFound, "Function with 1 argument should not be found");
    }

    @Test
    void testAddGhostFunction() {
        GhostFunction ghost = new GhostFunction("ghostFunc", "TestClass");
        context.addGhostFunction(ghost);

        assertTrue(context.hasGhost("TestClass.ghostFunc"), "Ghost function should exist");
        assertFalse(context.hasGhost("nonexistent"), "Nonexistent ghost should not exist");
    }

    @Test
    void testGetGhosts() {
        GhostFunction ghost1 = new GhostFunction("ghost1", "TestClass");
        GhostFunction ghost2 = new GhostFunction("ghost2", "TestClass");

        context.addGhostFunction(ghost1);
        context.addGhostFunction(ghost2);

        List<GhostFunction> ghosts = context.getGhosts();
        assertEquals(2, ghosts.size(), "Should have 2 ghost functions");
    }

    @Test
    void testAddGhostClass() {
        context.addGhostClass("TestClass");

        List<GhostState> states = context.getGhostState("TestClass");
        assertNotNull(states, "Ghost class state list should not be null");
        assertEquals(0, states.size(), "Ghost class should have no states initially");
    }

    @Test
    void testAddToGhostClass() {
        context.addGhostClass("TestClass");

        GhostState state = new GhostState("TestClass", "testState", null, null);
        context.addToGhostClass("TestClass", state);

        List<GhostState> states = context.getGhostState("TestClass");
        assertEquals(1, states.size(), "Should have 1 ghost state");
        assertTrue(states.contains(state), "State list should contain the added state");
    }

    @Test
    void testGetGhostState() {
        context.addGhostClass("TestClass");

        GhostState state = new GhostState("TestClass", "testState", null, null);
        context.addToGhostClass("TestClass", state);

        List<GhostState> states = context.getGhostState("TestClass");
        assertNotNull(states, "Ghost states should not be null");
        assertEquals(1, states.size(), "Should have 1 ghost state");
    }

    @Test
    void testGetAllGhostStates() {
        context.addGhostClass("Class1");
        context.addGhostClass("Class2");

        GhostState state1 = new GhostState("Class1", "state1", null, null);
        GhostState state2 = new GhostState("Class2", "state2", null, null);

        context.addToGhostClass("Class1", state1);
        context.addToGhostClass("Class2", state2);

        List<GhostState> allStates = context.getGhostState();
        assertEquals(2, allStates.size(), "Should have 2 ghost states total");
    }

    @Test
    void testAddAlias() {
        Predicate pred = new Predicate();
        AliasWrapper alias = new AliasWrapper("testAlias", pred, List.of("arg1"), List.of("String"));
        context.addAlias(alias);

        List<AliasWrapper> aliases = context.getAlias();
        assertEquals(1, aliases.size(), "Should have 1 alias");
        assertTrue(aliases.contains(alias), "Alias list should contain the added alias");
    }

    @Test
    void testGetAlias() {
        Predicate pred = new Predicate();
        AliasWrapper alias1 = new AliasWrapper("alias1", pred, List.of(), List.of());
        AliasWrapper alias2 = new AliasWrapper("alias2", pred, List.of(), List.of());

        context.addAlias(alias1);
        context.addAlias(alias2);

        List<AliasWrapper> aliases = context.getAlias();
        assertEquals(2, aliases.size(), "Should have 2 aliases");
    }

    @Test
    void testToString() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);

        String result = context.toString();
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("Variables"), "toString should contain 'Variables'");
        assertTrue(result.contains("Functions"), "toString should contain 'Functions'");
    }

    @Test
    void testAllVariablesToString() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        context.addVarToContext("x", intType, pred, null);
        context.addVarToContext("y", intType, pred, null);

        String result = context.allVariablesToString();
        assertNotNull(result, "allVariablesToString should not return null");
        assertTrue(result.contains("x"), "Result should contain variable x");
        assertTrue(result.contains("y"), "Result should contain variable y");
    }
}
