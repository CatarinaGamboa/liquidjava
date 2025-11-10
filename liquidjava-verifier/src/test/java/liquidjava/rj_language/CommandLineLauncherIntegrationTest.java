package liquidjava.verifier.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;

public class CommandLineLauncherIntegrationTest {

    private static final String VERIFIER_MAIN = "liquidjava.api.CommandLineLauncher";

    @BeforeAll
    static void setup() {
        // se necessário: compilar os exemplos ou garantir que os ficheiros de teste existem
    }

    @Test
    void testCorrectExampleShouldPassVerification() throws Exception {
        // supondo que existe no projecto um ficheiro de exemplo “CorrectSimpleAssignment.java”
        Path example = Paths.get("liquidjava-example/src/main/java/testSuite/CorrectSimpleAssignment.java");
        assertTrue(Files.exists(example), "Ficheiro de exemplo correcto não encontrado: " + example);

        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-cp",
            System.getProperty("java.class.path"),
            VERIFIER_MAIN,
            example.toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            output = reader.lines().reduce("", (a,b) -> a + b + System.lineSeparator());
        }
        int exitCode = process.waitFor();
        // verificar que passou
        assertEquals(0, exitCode, "Exit code devia ser 0 para caso correcto. Output:\n" + output);
        assertTrue(output.contains("Correct! Passed Verification") || output.contains("Passed Verification"),
                   "Output inesperado para caso correcto: " + output);
    }

    @Test
    void testErrorExampleShouldFailVerification() throws Exception {
        // supondo que existe o ficheiro “ErrorSimpleAssignment.java”
        Path example = Paths.get("liquidjava-example/src/main/java/testSuite/ErrorSimpleAssignment.java");
        assertTrue(Files.exists(example), "Ficheiro de exemplo de erro não encontrado: " + example);

        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-cp",
            System.getProperty("java.class.path"),
            VERIFIER_MAIN,
            example.toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            output = reader.lines().reduce("", (a,b) -> a + b + System.lineSeparator());
        }
        int exitCode = process.waitFor();
        // verificar que falhou
        assertNotEquals(0, exitCode, "Exit code devia ser diferente de 0 para caso de erro. Output:\n" + output);
        assertTrue(output.toLowerCase().contains("error") || output.contains("Refinement violation"),
                   "Output inesperado para caso de erro: " + output);
    }
}
