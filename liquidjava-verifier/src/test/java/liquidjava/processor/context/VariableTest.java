package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import liquidjava.rj_language.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the Variable class
 */
class VariableTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
    }

    @Test
    void testConstructor() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);

        assertNotNull(var, "Variable should not be null");
        assertEquals("x", var.getName(), "Variable name should be 'x'");
        assertEquals(intType, var.getType(), "Variable type should match");
    }

    @Test
    void testConstructorWithLocation() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", "TestClass.method", intType, pred);

        assertNotNull(var, "Variable should not be null");
        assertEquals("x", var.getName(), "Variable name should be 'x'");
        assertTrue(var.getLocation().isPresent(), "Location should be present");
        assertEquals("TestClass.method", var.getLocation().get(), "Location should match");
    }

    @Test
    void testSetAndGetLocation() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);
        assertFalse(var.getLocation().isPresent(), "Location should not be present initially");

        var.setLocation("MyLocation");
        assertTrue(var.getLocation().isPresent(), "Location should be present after setting");
        assertEquals("MyLocation", var.getLocation().get(), "Location should match");
    }

    @Test
    void testGetRefinement() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = Predicate.createVar("x");

        Variable var = new Variable("x", intType, pred);
        Predicate refinement = var.getRefinement();

        assertNotNull(refinement, "Refinement should not be null");
    }

    @Test
    void testGetMainRefinement() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = Predicate.createVar("x");

        Variable var = new Variable("x", intType, pred);
        Predicate mainRefinement = var.getMainRefinement();

        assertNotNull(mainRefinement, "Main refinement should not be null");
        assertEquals(pred.toString(), mainRefinement.toString(), "Main refinement should match original");
    }

    @Test
    void testAddInstance() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);
        VariableInstance instance = new VariableInstance("x_1", intType, pred);

        var.addInstance(instance);
        Optional<VariableInstance> lastInstance = var.getLastInstance();

        assertTrue(lastInstance.isPresent(), "Last instance should be present");
        assertEquals(instance, lastInstance.get(), "Last instance should match added instance");
    }

    @Test
    void testGetLastInstance() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);

        Optional<VariableInstance> emptyInstance = var.getLastInstance();
        assertFalse(emptyInstance.isPresent(), "Should not have instance initially");

        VariableInstance instance = new VariableInstance("x_1", intType, pred);
        var.addInstance(instance);

        Optional<VariableInstance> lastInstance = var.getLastInstance();
        assertTrue(lastInstance.isPresent(), "Should have instance after adding");
        assertEquals(instance, lastInstance.get(), "Last instance should match");
    }

    @Test
    void testEnterAndExitContext() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);

        VariableInstance instance1 = new VariableInstance("x_1", intType, pred);
        var.addInstance(instance1);
        assertEquals(instance1, var.getLastInstance().get(), "Should have first instance");

        // Enter new context
        var.enterContext();

        VariableInstance instance2 = new VariableInstance("x_2", intType, pred);
        var.addInstance(instance2);
        assertEquals(instance2, var.getLastInstance().get(), "Should have second instance in new context");

        // Exit context
        var.exitContext();

        Optional<VariableInstance> afterExit = var.getLastInstance();
        assertTrue(afterExit.isPresent(), "Should still have first instance after exit");
        assertEquals(instance1, afterExit.get(), "Should return to first instance after exit");
    }

    @Test
    void testMultipleInstances() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);

        VariableInstance instance1 = new VariableInstance("x_1", intType, pred);
        VariableInstance instance2 = new VariableInstance("x_2", intType, pred);
        VariableInstance instance3 = new VariableInstance("x_3", intType, pred);

        var.addInstance(instance1);
        var.addInstance(instance2);
        var.addInstance(instance3);

        Optional<VariableInstance> lastInstance = var.getLastInstance();
        assertTrue(lastInstance.isPresent(), "Last instance should be present");
        assertEquals(instance3, lastInstance.get(), "Last instance should be the most recently added");
    }

    @Test
    void testToString() {
        CtTypeReference<Integer> intType = factory.Type().integerPrimitiveType();
        Predicate pred = new Predicate();

        Variable var = new Variable("x", intType, pred);
        String str = var.toString();

        assertNotNull(str, "toString should not return null");
        assertTrue(str.contains("x"), "toString should contain variable name");
    }
}
