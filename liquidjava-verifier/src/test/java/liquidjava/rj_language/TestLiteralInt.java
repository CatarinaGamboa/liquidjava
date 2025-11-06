package liquidjava.rj_language;

import static org.junit.Assert.*;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;

public class TestLiteralInt {

    @Test
    public void testLiteralIntEquality() {
        LiteralInt a = new LiteralInt(10);
        LiteralInt b = new LiteralInt(10);
        assertEquals(a, b);
    }

    @Test
    public void testLiteralIntHashCodeConsistency() {
        LiteralInt a = new LiteralInt(5);
        int firstHash = a.hashCode();
        int secondHash = a.hashCode();
        assertEquals(firstHash, secondHash);

    }
}
