package liquidjava.rj_language.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestHashLitReal {

    @Test
    public void sameValue_sameHashCode_andEquals() {
        LiteralReal a = new LiteralReal(3.14);
        LiteralReal b = new LiteralReal(3.14);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void differentValues_differentHashCodes() {
        LiteralReal a = new LiteralReal(1.0);
        LiteralReal b = new LiteralReal(2.0);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void clone_preservesEqualityAndHashCode() {
        LiteralReal a = new LiteralReal(-42.5);
        LiteralReal c = (LiteralReal) a.clone();
        assertEquals(a, c);
        assertEquals(a.hashCode(), c.hashCode());
    }
}
