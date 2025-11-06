import static org.junit.Assert.*;

import org.junit.Test;

import liquidjava.rj_language.ast.LiteralBoolean;

public class TestLiteralBooleanSmall {

    @Test
    public void fromStringConstructor() {
        assertFalse(new LiteralBoolean("false").isBooleanTrue());
    }

    @Test
    public void cloneEquals() {
        LiteralBoolean lb = new LiteralBoolean(true);
        assertEquals(lb, lb.clone());
    }
}