import static org.junit.Assert.*;

import org.junit.Test;

import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiterals {

    @Test
    public void literalIntFromStringAndClone() {
        LiteralInt li = new LiteralInt("7");
        assertEquals(7, li.getValue());
        assertEquals(li, li.clone());
    }

    @Test
    public void literalStringToStringAndEquals() {
        LiteralString ls = new LiteralString("hello");
        assertEquals("hello", ls.toString());
        assertEquals(ls, ls.clone());
    }
}