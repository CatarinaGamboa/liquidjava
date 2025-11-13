package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import liquidjava.rj_language.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the RefinedFunction class
 */
class RefinedFunctionTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
        Context.getInstance().reinitializeAllContext();
    }

    @Test
    void testDefaultConstructor() {
        RefinedFunction func = new RefinedFunction();
        assertNotNull(func, "RefinedFunction should not be null");
        assertNotNull(func.getArguments(), "Arguments list should not be null");
        assertEquals(0, func.getArguments().size(), "Arguments list should be empty");
    }

    @Test
    void testConstructorWithParameters() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);

        assertNotNull(func, "RefinedFunction should not be null");
        assertEquals("testFunc", func.getName(), "Function name should match");
        assertEquals("TestClass", func.getTargetClass(), "Target class should match");
        assertEquals(intType, func.getType(), "Return type should match");
    }

    @Test
    void testAddArgRefinements() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction();
        func.addArgRefinements("arg1", intType, pred);
        func.addArgRefinements("arg2", intType, pred);

        List<Variable> args = func.getArguments();
        assertEquals(2, args.size(), "Should have 2 arguments");
        assertEquals("arg1", args.get(0).getName(), "First argument name should be 'arg1'");
        assertEquals("arg2", args.get(1).getName(), "Second argument name should be 'arg2'");
    }

    @Test
    void testAddArgRefinementsWithVariable() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("myArg", intType, pred);
        RefinedFunction func = new RefinedFunction();
        func.addArgRefinements(var);

        List<Variable> args = func.getArguments();
        assertEquals(1, args.size(), "Should have 1 argument");
        assertEquals(var, args.get(0), "Argument should be the added variable");
    }

    @Test
    void testGetArguments() {
        RefinedFunction func = new RefinedFunction();
        List<Variable> args = func.getArguments();

        assertNotNull(args, "Arguments list should not be null");
        assertEquals(0, args.size(), "Arguments list should be empty initially");
    }

    @Test
    void testSetAndGetClass() {
        RefinedFunction func = new RefinedFunction();
        func.setClass("MyClass");

        assertEquals("MyClass", func.getTargetClass(), "Target class should be 'MyClass'");
    }

    @Test
    void testGetTargetClass() {
        RefinedFunction func = new RefinedFunction("testFunc", "TargetClass", null, new Predicate());
        assertEquals("TargetClass", func.getTargetClass(), "Target class should match");
    }

    @Test
    void testSetAndGetRefReturn() {
        Predicate pred1 = new Predicate();
        Predicate pred2 = Predicate.createVar("result");

        RefinedFunction func = new RefinedFunction();
        func.setRefReturn(pred1);

        assertEquals(pred1.toString(), func.getRefReturn().toString(), "Return refinement should match");

        func.setRefReturn(pred2);
        assertEquals(pred2.toString(), func.getRefReturn().toString(), "Updated return refinement should match");
    }

    @Test
    void testSetAndGetSignature() {
        RefinedFunction func = new RefinedFunction();
        String signature = "int testFunc(int x, int y)";
        func.setSignature(signature);

        assertEquals(signature, func.getSignature(), "Signature should match");
    }

    @Test
    void testGetAllRefinements() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        Predicate allRefinements = func.getAllRefinements();

        assertNotNull(allRefinements, "All refinements should not be null");
    }

    @Test
    void testToString() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("testFunc", "TestClass", intType, pred);
        func.addArgRefinements("arg1", intType, pred);

        String str = func.toString();
        assertNotNull(str, "toString should not return null");
        assertTrue(str.contains("testFunc"), "toString should contain function name");
    }

    @Test
    void testMultipleArguments() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        CtTypeReference<String> stringType = factory.Type().stringType();
        Predicate pred = new Predicate();

        RefinedFunction func = new RefinedFunction("multiArgFunc", "TestClass", intType, pred);
        func.addArgRefinements("arg1", intType, pred);
        func.addArgRefinements("arg2", stringType, pred);
        func.addArgRefinements("arg3", intType, pred);

        List<Variable> args = func.getArguments();
        assertEquals(3, args.size(), "Should have 3 arguments");
        assertEquals("arg1", args.get(0).getName(), "First argument should be 'arg1'");
        assertEquals("arg2", args.get(1).getName(), "Second argument should be 'arg2'");
        assertEquals("arg3", args.get(2).getName(), "Third argument should be 'arg3'");
        assertEquals(intType, args.get(0).getType(), "First argument type should be int");
        assertEquals(stringType, args.get(1).getType(), "Second argument type should be String");
    }
}
