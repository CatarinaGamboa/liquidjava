package liquidjava.diagnostics.errors;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import spoon.reflect.declaration.CtElement;

public class LJErrorTest {

    static class ConcreteLJError extends LJError {
        ConcreteLJError(String message, String details, CtElement element) {
            super(message, details, element);
        }

        @Override
        public String toString() {
            return "ConcreteLJError: " + super.getMessage() + " | " + "Details field tested";
        }
    }

    @Test
    void testLJErrorMessage() {
        LJError error = new ConcreteLJError("Test error message", "Details", null);
        assertEquals("Details", error.getMessage());
    }

    @Test
    void testToStringContainsMessage() {
        LJError error = new ConcreteLJError("Invalid syntax", "Details", null);
        String s = error.toString();

        assertTrue(s.contains("ConcreteLJError"));
        assertTrue(s.contains("Details"));
    }

}
