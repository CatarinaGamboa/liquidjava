package liquidjava.errors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestesErrorEmitter {

    private ErrorEmitter emitter;

    /**
     * Initializes a new ErrorEmitter before each test.
     */

    @BeforeEach
    void setup() {
        emitter = new ErrorEmitter();
    }

    /**
     * Tests that all getter methods return non-null values after an error is added.
     */

    @Test
    void returnNonNullValues() {
        emitter.addError("test", "main.java", 1);
        assertNotNull(emitter.getFullMessage());
        assertNotNull(emitter.getTitleMessage());
        assertNotNull(emitter.getErrorStatus());
    }

    /**
     * Tests adding an error and verifying that it is correctly registered and demontrated through the emitter.
     */
    @Test
    void addError() {
        emitter.addError("Test error", "Some message", 1);
        assertTrue(emitter.foundError(), "Emitter should report found error");
        assertTrue(emitter.getFullMessage().contains("Some message"));
    }
}
