package liquidjava.integration;

import liquidjava.errors.ErrorEmitter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration test that verifies interaction between the ErrorEmitter class and the simulated error reporting
 * process.
 */
public class TesteIntegracao {

    @Test
    void shouldEmitAndRetrieveErrorInfo() {
        ErrorEmitter emitter = new ErrorEmitter();

        emitter.addError("Integration test error", "FakeFile.java", 42);

        assertTrue(emitter.foundError(), "Emitter should register an error");
        assertNotNull(emitter.getFullMessage(), "Full message should not be null");

        try {
            emitter.getFilePath();
        } catch (Exception e) {
            fail("getFilePath() should not throw an exception");
        }
    }
}
