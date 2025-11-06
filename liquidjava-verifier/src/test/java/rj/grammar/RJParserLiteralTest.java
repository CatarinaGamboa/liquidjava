package rj.grammar;

import org.antlr.v4.runtime.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RJParserLiteralTest {

    @Test
    void testIntegerLiteral() {
        String input = "123";
        RJLexer lexer = new RJLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RJParser parser = new RJParser(tokens);

        RJParser.LiteralContext ctx = parser.literal();

        assertNotNull(ctx);
        assertEquals("123", ctx.getText());
    }

    @Test
    void testBooleanLiteral() {
        String input = "true";
        RJLexer lexer = new RJLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RJParser parser = new RJParser(tokens);

        RJParser.LiteralContext ctx = parser.literal();

        assertNotNull(ctx);
        assertEquals("true", ctx.getText());
    }
}