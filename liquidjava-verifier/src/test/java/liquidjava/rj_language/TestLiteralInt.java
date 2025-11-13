package liquidjava.rj_language;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import liquidjava.rj_language.ast.LiteralString;

public class TestLiteralInt{
    @Test
    public void testLiteralInt(){
      LiteralInt s1 = new LiteralInt(10);
      LiteralInt s2 = new LiteralInt(20);
      assertNotEquals(s1.hashCode(), s2.hashCode());  
    }

}