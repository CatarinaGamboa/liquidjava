package rj.grammar;

import org.antlr.v4.runtime.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RJParserTest {

    @Test
    void testLiteralParsing() {
        String input = "42";
        RJLexer lexer = new RJLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RJParser parser = new RJParser(tokens);

        RJParser.LiteralContext context = parser.literal();

        assertNotNull(context);
        assertEquals("42", context.getText());
    }
}