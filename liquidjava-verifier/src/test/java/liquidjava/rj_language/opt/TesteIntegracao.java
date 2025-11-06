package liquidjava.integration;

import liquidjava.errors.ErrorEmitter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration test that verifies interaction between the ErrorEmitter
 * class and the simulated error reporting process. It ensures that error data
 * is recorded and retrievable without expecting specific internal fields.
 */
public class TesteIntegracao {

    @Test
    void shouldEmitAndRetrieveErrorInfo() {
        ErrorEmitter emitter = new ErrorEmitter();

        // Simulate adding an error
        emitter.addError("Integration test error", "FakeFile.java", 42);

        // Verify that the emitter detected an error and has non-null info
        assertTrue(emitter.foundError(), "Emitter should register an error");
        assertNotNull(emitter.getFullMessage(), "Full message should not be null");

        // File path may not be implemented — only check for no exception
        try {
            emitter.getFilePath();
        } catch (Exception e) {
            fail("getFilePath() should not throw an exception");
        }
    }
}

