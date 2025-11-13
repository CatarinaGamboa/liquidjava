package liquidjava.processor.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the GhostFunction class
 */
class GhostFunctionTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
    }

    @Test
    void testConstructorWithParameters() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        CtTypeReference<?> returnType = factory.Type().stringType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example.TestClass");

        assertNotNull(ghost, "GhostFunction should not be null");
        assertEquals("myGhost", ghost.getName(), "Name should be 'myGhost'");
        assertEquals(returnType, ghost.getReturnType(), "Return type should match");
        assertEquals(1, ghost.getParametersTypes().size(), "Should have 1 parameter");
    }

    @Test
    void testGetName() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("testGhost", paramTypes, returnType, "TestClass");
        assertEquals("testGhost", ghost.getName(), "Name should be 'testGhost'");
    }

    @Test
    void testGetReturnType() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().booleanPrimitiveType();

        GhostFunction ghost = new GhostFunction("testGhost", paramTypes, returnType, "TestClass");
        assertEquals(returnType, ghost.getReturnType(), "Return type should match");
    }

    @Test
    void testGetParametersTypes() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        paramTypes.add(factory.Type().stringType());
        CtTypeReference<?> returnType = factory.Type().booleanPrimitiveType();

        GhostFunction ghost = new GhostFunction("testGhost", paramTypes, returnType, "TestClass");
        List<CtTypeReference<?>> retrievedParams = ghost.getParametersTypes();

        assertEquals(2, retrievedParams.size(), "Should have 2 parameters");
        assertEquals(paramTypes.get(0), retrievedParams.get(0), "First parameter should match");
        assertEquals(paramTypes.get(1), retrievedParams.get(1), "Second parameter should match");
    }

    @Test
    void testGetPrefix() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("testGhost", paramTypes, returnType, "com.example.TestClass");
        assertEquals("com.example.TestClass", ghost.getPrefix(), "Prefix should match");
    }

    @Test
    void testGetQualifiedName() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example");
        String qualifiedName = ghost.getQualifiedName();

        assertTrue(qualifiedName.contains("myGhost"), "Qualified name should contain function name");
        assertTrue(qualifiedName.contains("com.example"), "Qualified name should contain prefix");
    }

    @Test
    void testGetParentClassName() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example.TestClass");
        String parentClassName = ghost.getParentClassName();

        assertEquals("TestClass", parentClassName, "Parent class name should be 'TestClass'");
    }

    @Test
    void testMatchesFullyQualifiedName() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example");
        String qualifiedName = ghost.getQualifiedName();

        assertTrue(ghost.matches(qualifiedName), "Should match fully qualified name");
    }

    @Test
    void testMatchesSimpleName() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example");
        assertTrue(ghost.matches("myGhost"), "Should match simple name");
    }

    @Test
    void testMatchesWithDifferentPrefix() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example");
        assertTrue(ghost.matches("com.other.myGhost"), "Should match name with different prefix");
    }

    @Test
    void testDoesNotMatch() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "com.example");
        assertFalse(ghost.matches("otherGhost"), "Should not match different name");
    }

    @Test
    void testToString() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        CtTypeReference<?> returnType = factory.Type().stringType();

        GhostFunction ghost = new GhostFunction("myGhost", paramTypes, returnType, "TestClass");
        String str = ghost.toString();

        assertNotNull(str, "toString should not return null");
        assertTrue(str.contains("ghost"), "toString should contain 'ghost'");
        assertTrue(str.contains("myGhost"), "toString should contain function name");
    }

    @Test
    void testNoParameters() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        CtTypeReference<?> returnType = factory.Type().integerPrimitiveType();

        GhostFunction ghost = new GhostFunction("noParamGhost", paramTypes, returnType, "TestClass");
        assertEquals(0, ghost.getParametersTypes().size(), "Should have no parameters");
    }

    @Test
    void testMultipleParameters() {
        List<CtTypeReference<?>> paramTypes = new ArrayList<>();
        paramTypes.add(factory.Type().integerPrimitiveType());
        paramTypes.add(factory.Type().stringType());
        paramTypes.add(factory.Type().booleanPrimitiveType());
        CtTypeReference<?> returnType = factory.Type().stringType();

        GhostFunction ghost = new GhostFunction("multiParam", paramTypes, returnType, "TestClass");
        assertEquals(3, ghost.getParametersTypes().size(), "Should have 3 parameters");
    }
}
