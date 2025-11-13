package liquidjava.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spoon.Launcher;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

/**
 * Test suite for the Utils class
 */
class UtilsTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        Launcher launcher = new Launcher();
        factory = launcher.getFactory();
    }

    @Test
    void testGetTypeInt() {
        CtTypeReference<?> type = Utils.getType("int", factory);
        assertNotNull(type, "Type should not be null");
        assertTrue(type.isPrimitive(), "int type should be primitive");
        assertEquals("int", type.getSimpleName(), "Type name should be 'int'");
    }

    @Test
    void testGetTypeDouble() {
        CtTypeReference<?> type = Utils.getType("double", factory);
        assertNotNull(type, "Type should not be null");
        assertTrue(type.isPrimitive(), "double type should be primitive");
        assertEquals("double", type.getSimpleName(), "Type name should be 'double'");
    }

    @Test
    void testGetTypeBoolean() {
        CtTypeReference<?> type = Utils.getType("boolean", factory);
        assertNotNull(type, "Type should not be null");
        assertTrue(type.isPrimitive(), "boolean type should be primitive");
        assertEquals("boolean", type.getSimpleName(), "Type name should be 'boolean'");
    }

    @Test
    void testGetTypeString() {
        CtTypeReference<?> type = Utils.getType("String", factory);
        assertNotNull(type, "Type should not be null");
        assertEquals("String", type.getSimpleName(), "Type name should be 'String'");
    }

    @Test
    void testGetTypeList() {
        CtTypeReference<?> type = Utils.getType("List", factory);
        assertNotNull(type, "Type should not be null");
        assertEquals("List", type.getSimpleName(), "Type name should be 'List'");
    }

    @Test
    void testGetTypeIntList() {
        CtTypeReference<?> type = Utils.getType("int[]", factory);
        assertNotNull(type, "Type should not be null");
        assertTrue(type.isArray(), "int[] type should be an array");
    }

    @Test
    void testGetTypeCustomClass() {
        CtTypeReference<?> type = Utils.getType("CustomClass", factory);
        assertNotNull(type, "Type should not be null");
        assertEquals("CustomClass", type.getSimpleName(), "Type name should be 'CustomClass'");
    }

    @Test
    void testGetSimpleNameWithDots() {
        String simpleName = Utils.getSimpleName("com.example.MyClass");
        assertEquals("MyClass", simpleName, "Simple name should be 'MyClass'");
    }

    @Test
    void testGetSimpleNameWithoutDots() {
        String simpleName = Utils.getSimpleName("MyClass");
        assertEquals("MyClass", simpleName, "Simple name should be 'MyClass'");
    }

    @Test
    void testGetSimpleNameWithMultipleDots() {
        String simpleName = Utils.getSimpleName("com.example.package.MyClass");
        assertEquals("MyClass", simpleName, "Simple name should be 'MyClass'");
    }

    @Test
    void testGetSimpleNameEmptyString() {
        String simpleName = Utils.getSimpleName("");
        assertEquals("", simpleName, "Simple name should be empty string");
    }

    @Test
    void testGetSimpleNameWithTrailingDot() {
        String simpleName = Utils.getSimpleName("com.example.");
        assertEquals("", simpleName, "Simple name should be empty after trailing dot");
    }

    @Test
    void testQualifyNameRegular() {
        String qualified = Utils.qualifyName("com.example", "MyClass");
        assertEquals("com.example.MyClass", qualified, "Qualified name should be 'com.example.MyClass'");
    }

    @Test
    void testQualifyNameDefaultOld() {
        String qualified = Utils.qualifyName("com.example", "old");
        assertEquals("old", qualified, "Default name 'old' should not be qualified");
    }

    @Test
    void testQualifyNameDefaultLength() {
        String qualified = Utils.qualifyName("com.example", "length");
        assertEquals("length", qualified, "Default name 'length' should not be qualified");
    }

    @Test
    void testQualifyNameDefaultAddToIndex() {
        String qualified = Utils.qualifyName("com.example", "addToIndex");
        assertEquals("addToIndex", qualified, "Default name 'addToIndex' should not be qualified");
    }

    @Test
    void testQualifyNameDefaultGetFromIndex() {
        String qualified = Utils.qualifyName("com.example", "getFromIndex");
        assertEquals("getFromIndex", qualified, "Default name 'getFromIndex' should not be qualified");
    }

    @Test
    void testQualifyNameWithEmptyPrefix() {
        String qualified = Utils.qualifyName("", "MyClass");
        assertEquals(".MyClass", qualified, "Qualified name should be '.MyClass'");
    }

    @Test
    void testStripParensWithParens() {
        String result = Utils.stripParens("(expression)");
        assertEquals("expression", result, "Parentheses should be stripped");
    }

    @Test
    void testStripParensWithoutParens() {
        String result = Utils.stripParens("expression");
        assertEquals("expression", result, "String without parens should remain unchanged");
    }

    @Test
    void testStripParensOnlyOpeningParen() {
        String result = Utils.stripParens("(expression");
        assertEquals("(expression", result, "String with only opening paren should remain unchanged");
    }

    @Test
    void testStripParensOnlyClosingParen() {
        String result = Utils.stripParens("expression)");
        assertEquals("expression)", result, "String with only closing paren should remain unchanged");
    }

    @Test
    void testStripParensNestedParens() {
        String result = Utils.stripParens("((expression))");
        assertEquals("(expression)", result, "Only outer parens should be stripped");
    }

    @Test
    void testStripParensEmptyParens() {
        String result = Utils.stripParens("()");
        assertEquals("", result, "Empty parens should result in empty string");
    }

    @Test
    void testStripParensSingleChar() {
        String result = Utils.stripParens("(x)");
        assertEquals("x", result, "Single char in parens should be stripped");
    }
}
