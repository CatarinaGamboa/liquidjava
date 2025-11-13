package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import liquidjava.rj_language.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the GhostState class
 */
class GhostStateTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
    }

    @Test
    void testConstructor() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        CtTypeReference<?> returnType = factory.Type().stringType();

        GhostState state = new GhostState("myState", paramTypes, returnType, "com.example.TestClass");

        assertNotNull(state, "GhostState should not be null");
        assertEquals("myState", state.getName(), "Name should be 'myState'");
        assertEquals(returnType, state.getReturnType(), "Return type should match");
    }

    @Test
    void testSetAndGetGhostParent() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostState state = new GhostState("childState", paramTypes, returnType, "TestClass");
        GhostFunction parent = new GhostFunction("parentGhost", paramTypes, returnType, "TestClass");

        assertNull(state.getParent(), "Parent should be null initially");

        state.setGhostParent(parent);
        assertNotNull(state.getParent(), "Parent should not be null after setting");
        assertEquals(parent, state.getParent(), "Parent should match");
    }

    @Test
    void testSetAndGetRefinement() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostState state = new GhostState("myState", paramTypes, returnType, "TestClass");
        Predicate pred = Predicate.createVar("x");

        assertNull(state.getRefinement(), "Refinement should be null initially");

        state.setRefinement(pred);
        assertNotNull(state.getRefinement(), "Refinement should not be null after setting");
        assertEquals(pred.toString(), state.getRefinement().toString(), "Refinement should match");
    }

    @Test
    void testInheritsFromGhostFunction() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        CtTypeReference<?> returnType = factory.Type().booleanPrimitiveType();

        GhostState state = new GhostState("myState", paramTypes, returnType, "TestClass");

        // Test inherited methods
        assertEquals("myState", state.getName(), "Should inherit getName()");
        assertEquals(returnType, state.getReturnType(), "Should inherit getReturnType()");
        assertEquals(1, state.getParametersTypes().size(), "Should inherit getParametersTypes()");
    }

    @Test
    void testMatchesInherited() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostState state = new GhostState("myState", paramTypes, returnType, "com.example");

        assertTrue(state.matches("myState"), "Should match simple name");
        assertTrue(state.matches(state.getQualifiedName()), "Should match qualified name");
    }

    @Test
    void testRefinementWithComplexPredicate() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostState state = new GhostState("myState", paramTypes, returnType, "TestClass");

        // Create complex predicate: (x > 5) && (y < 10)
        Predicate x = Predicate.createVar("x");
        Predicate five = Predicate.createLit("5", "int");
        Predicate xGreater5 = Predicate.createOperation(x, ">", five);

        Predicate y = Predicate.createVar("y");
        Predicate ten = Predicate.createLit("10", "int");
        Predicate yLess10 = Predicate.createOperation(y, "<", ten);

        Predicate complex = Predicate.createConjunction(xGreater5, yLess10);

        state.setRefinement(complex);

        assertNotNull(state.getRefinement(), "Refinement should not be null");
        String refinementStr = state.getRefinement().toString();
        assertTrue(refinementStr.contains("x") && refinementStr.contains("y"),
                   "Refinement should contain both variables");
    }

    @Test
    void testParentChildRelationship() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction parent = new GhostFunction("parentGhost", paramTypes, returnType, "TestClass");
        GhostState child = new GhostState("childState", paramTypes, returnType, "TestClass");

        child.setGhostParent(parent);

        assertEquals(parent, child.getParent(), "Child should have correct parent");
        assertEquals(parent.getName(), child.getParent().getName(), "Parent name should match");
    }

    @Test
    void testStateWithNoParameters() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().booleanPrimitiveType();

        GhostState state = new GhostState("emptyState", paramTypes, returnType, "TestClass");

        assertEquals(0, state.getParametersTypes().size(), "Should have no parameters");
    }
}
