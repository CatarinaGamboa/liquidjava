package liquidjava.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import liquidjava.processor.context.*;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Integration tests for Context management with variables, functions, ghosts, and refinements
 * These tests verify that multiple components work together correctly in realistic scenarios
 */
class ContextIntegrationTest {

    private Context context;
    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
        context = Context.getInstance();
        context.reinitializeAllContext();
    }

    @Test
    void testCompleteVariableLifecycle() {
        // Scenario: Create variables, enter/exit contexts, track refinements
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate initialPred = Predicate.createOperation(
            Predicate.createVar("x"),
            ">",
            Predicate.createLit("0", "int")
        );

        // Add variable in global scope
        context.addVarToContext("x", intType, initialPred, null);
        assertTrue(context.hasVariable("x"), "Variable should exist in global scope");

        // Enter new scope and add local variable
        context.enterContext();
        Predicate localPred = Predicate.createOperation(
            Predicate.createVar("y"),
            "<",
            Predicate.createLit("100", "int")
        );
        context.addVarToContext("y", intType, localPred, null);

        assertTrue(context.hasVariable("x"), "Global variable accessible in nested scope");
        assertTrue(context.hasVariable("y"), "Local variable exists");
        assertEquals(2, context.getAllVariables().size(), "Should have 2 variables");

        // Update refinement for x
        Predicate newPred = Predicate.createOperation(
            Predicate.createVar("x"),
            ">=",
            Predicate.createLit("5", "int")
        );
        context.newRefinementToVariableInContext("x", newPred);
        assertEquals(newPred.toString(), context.getVariableRefinements("x").toString());

        // Exit scope
        context.exitContext();
        assertTrue(context.hasVariable("x"), "Global variable still exists");
        assertFalse(context.hasVariable("y"), "Local variable removed");
        assertEquals(1, context.getAllVariables().size(), "Only global variable remains");
    }

    @Test
    void testFunctionRegistrationAndRetrieval() {
        // Scenario: Register functions with refinements and retrieve them
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        CtTypeReference<String> stringType = factory.Type().stringType();

        // Create function with arguments and return refinement
        RefinedFunction func = new RefinedFunction("calculate", "MathUtils", intType, new Predicate());
        func.addArgRefinements("x", intType, Predicate.createOperation(
            Predicate.createVar("x"), ">", Predicate.createLit("0", "int")
        ));
        func.addArgRefinements("y", intType, Predicate.createOperation(
            Predicate.createVar("y"), ">", Predicate.createLit("0", "int")
        ));
        func.setRefReturn(Predicate.createOperation(
            Predicate.createVar("result"), ">", Predicate.createLit("0", "int")
        ));

        context.addFunctionToContext(func);

        // Retrieve and verify
        RefinedFunction retrieved = context.getFunction("calculate", "MathUtils", 2);
        assertNotNull(retrieved, "Function should be found by name, class, and arity");
        assertEquals(2, retrieved.getArguments().size(), "Should have 2 arguments");

        // Add another function with same name but different arity
        RefinedFunction func2 = new RefinedFunction("calculate", "MathUtils", intType, new Predicate());
        func2.addArgRefinements("x", intType, new Predicate());
        context.addFunctionToContext(func2);

        List<RefinedFunction> overloads = context.getAllMethodsWithNameSize("calculate", 1);
        assertEquals(1, overloads.size(), "Should find function with arity 1");

        overloads = context.getAllMethodsWithNameSize("calculate", 2);
        assertEquals(1, overloads.size(), "Should find function with arity 2");
    }

    @Test
    void testGhostFunctionsAndStates() {
        // Scenario: Register ghost functions and states, verify hierarchy
        GhostFunction ghost1 = new GhostFunction("ghostPredicate", List.of(), factory.Type().booleanPrimitiveType(), "TestClass");
        context.addGhostFunction(ghost1);
        assertTrue(context.hasGhost("TestClass.ghostPredicate"), "Should find ghost by qualified name");
        assertTrue(context.hasGhost("ghostPredicate"), "Should find ghost by simple name");

        // Add ghost class with states
        context.addGhostClass("StateManager");
        GhostState state1 = new GhostState("StateManager", "initialized", null, null);
        state1.setRefinement(Predicate.createLit("true", "boolean"));
        context.addToGhostClass("StateManager", state1);

        GhostState state2 = new GhostState("StateManager", "ready", null, null);
        state2.setRefinement(Predicate.createVar("initialized"));
        context.addToGhostClass("StateManager", state2);

        List<GhostState> states = context.getGhostState("StateManager");
        assertEquals(2, states.size(), "Should have 2 states");

        // Verify state refinements
        assertTrue(states.get(0).getRefinement().toString().contains("true"));
        assertTrue(states.get(1).getRefinement().toString().contains("initialized"));
    }

    @Test
    void testAliasManagement() {
        // Scenario: Register and use aliases for complex predicates
        Predicate complexPred = Predicate.createOperation(
            Predicate.createOperation(
                Predicate.createVar("x"),
                "*",
                Predicate.createVar("x")
            ),
            "+",
            Predicate.createOperation(
                Predicate.createVar("y"),
                "*",
                Predicate.createVar("y")
            )
        );

        AliasWrapper alias = new AliasWrapper("distanceSquared", complexPred,
            List.of("x", "y"), List.of("int", "int"));
        context.addAlias(alias);

        List<AliasWrapper> aliases = context.getAlias();
        assertEquals(1, aliases.size(), "Should have 1 alias");
        assertEquals("distanceSquared", aliases.get(0).getName());

        // Create new variables for substitution
        List<String> newVars = alias.getNewVariables(context);
        assertEquals(2, newVars.size(), "Should generate 2 new variable names");
        assertTrue(newVars.get(0).contains("alias_x"), "Generated name should contain original");
    }

    @Test
    void testVariableInstanceTracking() {
        // Scenario: Track variable instances through assignments and control flow
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate initialRefinement = Predicate.createVar("x");

        Variable var = new Variable("x", intType, initialRefinement);
        context.addVarToContext(var);

        // Simulate assignment: x = 5
        VariableInstance instance1 = new VariableInstance("x_1", intType,
            Predicate.createEquals(Predicate.createVar("x_1"), Predicate.createLit("5", "int")));
        var.addInstance(instance1);
        context.addSpecificVariable(instance1);
        context.addRefinementInstanceToVariable("x", "x_1");

        assertTrue(var.getLastInstance().isPresent(), "Should have instance");
        assertEquals("x_1", var.getLastInstance().get().getName());

        // Simulate second assignment: x = x + 1
        VariableInstance instance2 = new VariableInstance("x_2", intType,
            Predicate.createEquals(Predicate.createVar("x_2"),
                Predicate.createOperation(Predicate.createVar("x_1"), "+", Predicate.createLit("1", "int"))));
        var.addInstance(instance2);
        context.addSpecificVariable(instance2);
        context.addRefinementInstanceToVariable("x", "x_2");

        assertEquals("x_2", var.getLastInstance().get().getName(), "Latest instance should be x_2");
    }

    @Test
    void testIfBranchCombination() {
        // Scenario: Track variables through if-then-else branches
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();

        Variable var = new Variable("x", intType, Predicate.createVar("x"));
        context.addVarToContext(var);

        // Before if
        context.variablesSetBeforeIf();

        // Then branch: x = 10
        VariableInstance thenInstance = new VariableInstance("x_then", intType,
            Predicate.createEquals(Predicate.createVar("x_then"), Predicate.createLit("10", "int")));
        var.addInstance(thenInstance);
        context.variablesSetThenIf();

        // Else branch: x = 20
        VariableInstance elseInstance = new VariableInstance("x_else", intType,
            Predicate.createEquals(Predicate.createVar("x_else"), Predicate.createLit("20", "int")));
        var.addInstance(elseInstance);
        context.variablesSetElseIf();

        // Combine branches
        Predicate condition = Predicate.createVar("condition");
        context.variablesNewIfCombination();
        context.variablesCombineFromIf(condition);
        context.variablesFinishIfCombination();

        // Should create combined instance with ITE predicate
        assertTrue(context.hasVariable("x"), "Variable x should still exist");
    }

    @Test
    void testComplexScenarioWithMultipleComponents() {
        // Realistic scenario: Function with refinements, variables, and ghosts
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();

        // Register a ghost function for validation
        GhostFunction validationGhost = new GhostFunction("isValid",
            List.of(intType), factory.Type().booleanPrimitiveType(), "Validator");
        context.addGhostFunction(validationGhost);

        // Create function with precondition using ghost
        RefinedFunction processFunc = new RefinedFunction("process", "Processor", intType, new Predicate());
        Predicate precondition = Predicate.createInvocation("Validator.isValid", Predicate.createVar("input"));
        processFunc.addArgRefinements("input", intType, precondition);

        Predicate postcondition = Predicate.createOperation(
            Predicate.createVar("result"), ">=", Predicate.createVar("input")
        );
        processFunc.setRefReturn(postcondition);

        context.addFunctionToContext(processFunc);

        // Add variables with refinements
        context.addVarToContext("input", intType, precondition, null);
        context.addVarToContext("result", intType, postcondition, null);

        // Verify everything is integrated
        assertTrue(context.hasGhost("Validator.isValid"), "Ghost function registered");
        assertNotNull(context.getFunction("process", "Processor"), "Function registered");
        assertTrue(context.hasVariable("input"), "Input variable exists");
        assertTrue(context.hasVariable("result"), "Result variable exists");

        // Get all variables with supertypes (for subtyping checks)
        List<RefinedVariable> varsWithSupertypes = context.getAllVariablesWithSupertypes();
        assertNotNull(varsWithSupertypes, "Should return variables list");
    }

    @Test
    void testGlobalVariableManagement() {
        // Scenario: Global variables persist across context resets
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();

        context.addGlobalVariableToContext("GLOBAL_CONST", intType,
            Predicate.createEquals(Predicate.createVar("GLOBAL_CONST"), Predicate.createLit("42", "int")));

        assertTrue(context.hasVariable("GLOBAL_CONST"), "Global variable should exist");

        // Add local variable
        context.addVarToContext("local", intType, new Predicate(), null);
        assertEquals(2, context.getAllVariables().size(), "Should have both global and local");

        // Reinitialize context (not all)
        context.reinitializeContext();

        assertTrue(context.hasVariable("GLOBAL_CONST"), "Global variable persists");
        assertFalse(context.hasVariable("local"), "Local variable removed");
    }

    @Test
    void testCounterIncrement() {
        // Verify counter is used for unique variable generation
        int counter1 = context.getCounter();
        int counter2 = context.getCounter();
        int counter3 = context.getCounter();

        assertTrue(counter2 > counter1, "Counter should increment");
        assertTrue(counter3 > counter2, "Counter should continue incrementing");
        assertEquals(1, counter2 - counter1, "Should increment by 1");
    }
}
