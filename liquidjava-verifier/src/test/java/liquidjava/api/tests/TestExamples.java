package liquidjava.api.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import liquidjava.api.CommandLineLauncher;
import liquidjava.errors.ErrorEmitter;

public class TestExamples {

    @ParameterizedTest
    @MethodSource("fileNameSource")
    public void testFile(final Path fileName) {
        ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(fileName.toAbsolutePath().toString());
        if (fileName.startsWith("Correct") && errorEmitter.foundError()) {
            fail();
        }
        if (fileName.startsWith("Error") && !errorEmitter.foundError()) {
            fail();
        }
    }

    private static Stream<Path> fileNameSource() throws IOException {
        return Files.find(Paths.get("../liquidjava-example/src/main/java/testSuite/"), Integer.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile());
    }
}
