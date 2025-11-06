package liquidjava.rj_language.ast.typing;

import static org.junit.Assert.*;
import org.junit.Test;
import liquidjava.rj_language.ast.*;
import liquidjava.processor.context.Context;
import spoon.Launcher;
import spoon.reflect.factory.Factory;

public class TestCheckCompatibleType {

    @Test
    public void testCompatibleLiterals() {
        Launcher launcher = new Launcher();
        Factory factory = launcher.getFactory();
        Context ctx = null;

        LiteralInt a = new LiteralInt(3);
        LiteralInt b = new LiteralInt(4);

        assertTrue(TypeInfer.checkCompatibleType(a, b, ctx, factory));
    }

    @Test
    public void testIncompatibleLiterals() {
        Launcher launcher = new Launcher();
        Factory factory = launcher.getFactory();
        Context ctx = null;

        LiteralInt a = new LiteralInt(3);
        LiteralString s = new LiteralString("abc");

        assertFalse(TypeInfer.checkCompatibleType(a, s, ctx, factory));
    }
}
