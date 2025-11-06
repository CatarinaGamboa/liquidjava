package liquidjava.rj_language.ast.typing;

import static org.junit.Assert.*;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.factory.Factory;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.*;
import java.util.Optional;
import spoon.reflect.reference.CtTypeReference;

public class TestBoolType {

    @Test
    public void testBooleanLiteralType() {
        Launcher launcher = new Launcher();
        Factory factory = launcher.getFactory();
        Context ctx = null;

        LiteralBoolean boolExpr = new LiteralBoolean(true);
        Optional<CtTypeReference<?>> result = TypeInfer.getType(ctx, factory, boolExpr);

        assertTrue(result.isPresent());
        assertEquals("boolean", result.get().getSimpleName().toLowerCase());
    }
}
