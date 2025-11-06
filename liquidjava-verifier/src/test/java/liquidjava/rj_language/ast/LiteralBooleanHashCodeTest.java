package liquidjava.rj_language.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class LiteralBooleanHashCodeTest {

    @Test
    public void sameValue_sameHashCode_andEquals() {
        LiteralBoolean a = new LiteralBoolean(true);
        LiteralBoolean b = new LiteralBoolean(true);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void differentValues_differentHashCodes() {
        LiteralBoolean a = new LiteralBoolean(true);
        LiteralBoolean b = new LiteralBoolean(false);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void clone_preservesEqualityAndHashCode() {
        LiteralBoolean a = new LiteralBoolean(false);
        LiteralBoolean c = (LiteralBoolean) a.clone();
        assertEquals(a, c);
        assertEquals(a.hashCode(), c.hashCode());
    }
}
