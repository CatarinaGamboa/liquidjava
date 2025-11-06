package liquidjava.errors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestesErrorEmitter {

    private ErrorEmitter emitter;

    @BeforeEach
    void setup() {
        emitter = new ErrorEmitter();
    }

    @Test
    void returnNonNullValues() {
        emitter.addError("test", "main.java", 3);
        assertNotNull(emitter.getFullMessage());
        assertNotNull(emitter.getTitleMessage());
        assertNotNull(emitter.getErrorStatus());
    }

    @Test
    void addError() {
        emitter.addError("Test error", "File.java", 42);

        assertTrue(emitter.foundError(), "Emitter should report found error");
        assertEquals("File.java", emitter.getFilePath());
        assertTrue(emitter.getFullMessage().contains("Test error"));
    }
}
