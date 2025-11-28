package liquidjava.diagnostics;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestLJDiagnostic {
    @Test
    public void testGetTitle() {
        String title1 = "title";
        String title2 = "fake title";
        LJDiagnostic ljd = new LJDiagnostic(title1, null, null, null, null);

        assertTrue(ljd.getTitle().equals(title1));
        assertTrue(!ljd.getTitle().equals(title2));
    }
}
