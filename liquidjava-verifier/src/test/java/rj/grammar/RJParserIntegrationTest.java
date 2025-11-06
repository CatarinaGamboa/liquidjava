package rj.grammar;

import org.antlr.v4.runtime.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RJParserIntegrationTest {

    @Test
    void testFullProgramParsing() {
        String input = """
                type MyAlias(int x) {
                    !(x < 5)
                }
                ghost int myGhost(int y)
                """;

        RJLexer lexer = new RJLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RJParser parser = new RJParser(tokens);

        RJParser.ProgContext ctx = parser.prog();

        assertNotNull(ctx, "O contexto do programa não deve ser nulo");
        assertNotNull(ctx.start(), "Deve conter uma regra start válida");
        assertEquals(0, parser.getNumberOfSyntaxErrors(), "O parser não deve gerar erros");
    }
}