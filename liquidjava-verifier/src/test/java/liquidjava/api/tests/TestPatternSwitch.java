package liquidjava.api.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import liquidjava.api.CommandLineLauncher;
import liquidjava.diagnostics.Diagnostics;
import liquidjava.diagnostics.errors.LJError;

/**
 * Type-pattern switches need Java 21, above the level of liquidjava-example, so they are verified here in a project
 * that declares release 21 (#345).
 */
class TestPatternSwitch {

    @TempDir
    Path project;

    private Diagnostics verify(String className, String source) throws IOException {
        Files.writeString(project.resolve("pom.xml"),
                "<project><modelVersion>4.0.0</modelVersion><properties><maven.compiler.release>21"
                        + "</maven.compiler.release></properties></project>");
        Path src = Files.createDirectories(project.resolve("src"));
        Files.writeString(src.resolve(className + ".java"), source);
        CommandLineLauncher.launch(src.toString());
        return Diagnostics.getInstance();
    }

    @Test
    void verifiesTypePatternSwitch() throws IOException {
        Diagnostics diagnostics = verify("Kinds", """
                public class Kinds {
                    static String kind(Object o) {
                        return switch (o) {
                            case String s -> "string " + s;
                            case Integer i when i > 0 -> "positive";
                            default -> "other";
                        };
                    }

                    static int size(Object o) {
                        switch (o) {
                            case String s -> { return s.length(); }
                            default -> { return 0; }
                        }
                    }
                }
                """);
        assertTrue(diagnostics.getErrors().isEmpty(), diagnostics.getErrorOutput());
        assertTrue(diagnostics.getWarnings().isEmpty(), diagnostics.getWarningOutput());
    }

    @Test
    void checksRefinementsInsidePatternCase() throws IOException {
        Diagnostics diagnostics = verify("Cases", """
                import liquidjava.specification.Refinement;

                public class Cases {
                    static int positive(@Refinement("_ > 0") int x) {
                        return x;
                    }

                    static int kind(Object o) {
                        return switch (o) {
                            case String s -> positive(-1);
                            default -> 0;
                        };
                    }
                }
                """);
        assertEquals(1, diagnostics.getErrors().size(), diagnostics.getErrorOutput());
        LJError error = diagnostics.getErrors().iterator().next();
        assertEquals("Refinement Error", error.getTitle());
        assertEquals(10, error.getPosition().getLine());
    }
}
