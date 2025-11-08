package liquidjava.rj_language.ast.typing;

import static org.junit.Assert.*;
import org.junit.Test;
import liquidjava.rj_language.ast.*;
import liquidjava.processor.context.Context;
import spoon.Launcher;
import spoon.reflect.factory.Factory;
import java.util.Optional;
import spoon.reflect.reference.CtTypeReference;

public class TestBinaryTypeIntegration {

    @Test
    public void testBinaryIntExpressionType() {
        Launcher launcher = new Launcher();
        Factory factory = launcher.getFactory();
        Context ctx = null;

        BinaryExpression expr = new BinaryExpression(new LiteralInt(1), "+", new LiteralInt(2));
        Optional<CtTypeReference<?>> result = TypeInfer.getType(ctx, factory, expr);
        assertTrue(result.isPresent());
        assertEquals("int", result.get().getSimpleName().toLowerCase());
    }

    @Test
    public void testBinaryBooleanExpressionType() {
        Launcher launcher = new Launcher();
        Factory factory = launcher.getFactory();
        Context ctx = null;

        BinaryExpression expr = new BinaryExpression(new LiteralBoolean(true), "&&", new LiteralBoolean(false));
        Optional<CtTypeReference<?>> result = TypeInfer.getType(ctx, factory, expr);
        assertTrue(result.isPresent());
        assertEquals("boolean", result.get().getSimpleName().toLowerCase());
    }
}
