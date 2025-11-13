package liquidjava.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test suite for the Pair class
 */
class PairTest {

    @Test
    void testConstructor() {
        Pair<String, Integer> pair = new Pair<>("key", 42);
        assertNotNull(pair, "Pair should not be null");
    }

    @Test
    void testGetFirst() {
        Pair<String, Integer> pair = new Pair<>("key", 42);
        assertEquals("key", pair.getFirst(), "First element should be 'key'");
    }

    @Test
    void testGetSecond() {
        Pair<String, Integer> pair = new Pair<>("key", 42);
        assertEquals(42, pair.getSecond(), "Second element should be 42");
    }

    @Test
    void testGetFirstWithNull() {
        Pair<String, Integer> pair = new Pair<>(null, 42);
        assertNull(pair.getFirst(), "First element should be null");
    }

    @Test
    void testGetSecondWithNull() {
        Pair<String, Integer> pair = new Pair<>("key", null);
        assertNull(pair.getSecond(), "Second element should be null");
    }

    @Test
    void testToString() {
        Pair<String, Integer> pair = new Pair<>("key", 42);
        String str = pair.toString();

        assertNotNull(str, "toString should not return null");
        assertTrue(str.contains("key"), "toString should contain 'key'");
        assertTrue(str.contains("42"), "toString should contain '42'");
        assertTrue(str.contains("Pair"), "toString should contain 'Pair'");
    }

    @Test
    void testPairWithDifferentTypes() {
        Pair<Integer, String> pair = new Pair<>(100, "value");
        assertEquals(100, pair.getFirst(), "First element should be 100");
        assertEquals("value", pair.getSecond(), "Second element should be 'value'");
    }

    @Test
    void testPairWithSameTypes() {
        Pair<String, String> pair = new Pair<>("first", "second");
        assertEquals("first", pair.getFirst(), "First element should be 'first'");
        assertEquals("second", pair.getSecond(), "Second element should be 'second'");
    }

    @Test
    void testPairWithObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Pair<Object, Object> pair = new Pair<>(obj1, obj2);

        assertSame(obj1, pair.getFirst(), "First element should be the same object");
        assertSame(obj2, pair.getSecond(), "Second element should be the same object");
    }

    @Test
    void testPairImmutability() {
        String key = "key";
        Integer value = 42;
        Pair<String, Integer> pair = new Pair<>(key, value);

        // Verify that the pair holds the original values
        assertEquals(key, pair.getFirst(), "First element should match original");
        assertEquals(value, pair.getSecond(), "Second element should match original");
    }

    @Test
    void testToStringWithComplexTypes() {
        Pair<Integer[], String[]> pair = new Pair<>(new Integer[]{1, 2, 3}, new String[]{"a", "b"});
        String str = pair.toString();

        assertNotNull(str, "toString should not return null");
        assertTrue(str.contains("Pair"), "toString should contain 'Pair'");
    }
}
