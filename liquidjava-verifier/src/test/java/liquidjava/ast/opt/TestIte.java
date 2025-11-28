import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralInt;
import liquidjava.rj_language.ast.Ite;

public class TestIte {
    @Test
    public void testIte() {
        LiteralInt i1 = new LiteralInt(3);
        LiteralInt i2 = new LiteralInt(3);

        Ite ite1 = new Ite(i1, i1, i1);
        Ite ite2 = new Ite(i2, i2, i2);

        assertEquals(ite1.hashCode(), ite2.hashCode());
    }
}
