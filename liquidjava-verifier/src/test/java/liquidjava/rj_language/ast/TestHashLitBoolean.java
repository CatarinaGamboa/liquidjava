package liquidjava.rj_language.ast;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestHashLitBoolean {

    @Test
    void sameValue_sameHashCode_andEquals() {
        LiteralBoolean a = new LiteralBoolean(true);
        LiteralBoolean b = new LiteralBoolean(true);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentValues_differentHashCodes() {
        LiteralBoolean a = new LiteralBoolean(true);
        LiteralBoolean b = new LiteralBoolean(false);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void clone_preservesEqualityAndHashCode() {
        LiteralBoolean a = new LiteralBoolean(false);
        LiteralBoolean c = (LiteralBoolean) a.clone();
        assertEquals(a, c);
        assertEquals(a.hashCode(), c.hashCode());
    }
}
