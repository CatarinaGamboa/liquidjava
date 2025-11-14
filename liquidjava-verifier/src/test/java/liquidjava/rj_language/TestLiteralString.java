package liquidjava.rj_language;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralString {
    @Test
    public void testLiteralString() {
        // Arrange: Preparar os objetos
        LiteralString s1 = new LiteralString("hello");
        LiteralString s2 = new LiteralString("world");

        // Act & Assert: Executar e verificar
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testToString() {
        LiteralString myString = new LiteralString("olá");

        String resultado = myString.toString();

        assertEquals("olá", resultado);
    }

    @Test
    public void isBooleanTrue() {
        LiteralString myString = new LiteralString("yes");

        boolean result = myString.isBooleanTrue();

        assertFalse(result);

    }

}
