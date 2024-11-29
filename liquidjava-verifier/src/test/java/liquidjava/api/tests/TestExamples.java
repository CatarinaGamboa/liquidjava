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
    public void testFile(final Path filePath) {
        String fileName = filePath.getFileName().toString();
        System.out.println("Testing file: " + fileName);

        // For Regular Files in the root directory check their name
        if (Files.isRegularFile(filePath)) {
            ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(filePath.toAbsolutePath().toString());
            System.out.println(
                    errorEmitter.foundError() ? (errorEmitter.getFullMessage()) : ("Correct! Passed Verification."));

            if (fileName.startsWith("Correct") && errorEmitter.foundError()) {
                System.out.println("Error in directory: " + fileName + " --- should be correct but an error was found");
                fail();
            }
            if (fileName.startsWith("Error") && !errorEmitter.foundError()) {
                System.out.println("Error in directory: " + fileName + " --- should be an error but passed verification");
                fail();
            }
        } else // For Directories and subdirectories check if they contain "error" or "correct" in their name
            if (Files.isDirectory(filePath) && (fileName.contains("error") || fileName.contains("correct"))) {
            ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(filePath.toAbsolutePath().toString());
            System.out.println(
                    errorEmitter.foundError() ? (errorEmitter.getFullMessage()) : ("Correct! Passed Verification."));

            if (fileName.contains("correct") && errorEmitter.foundError()) {
                System.out.println("Error in directory: " + fileName + " --- should be correct but an error was found");
                fail();
            }
            if (fileName.contains("error") && !errorEmitter.foundError()) {
                System.out.println("Error in directory: " + fileName + " --- should be an error but passed verification");
                fail();
            }
        }
    }

    private static Stream<Path> fileNameSource() throws IOException {
        return Files.find(Paths.get("../liquidjava-example/src/main/java/testSuite/"), Integer.MAX_VALUE,
                (filePath, fileAttr) -> {
                    // Get the parent directory path (root directory)
                    Path rootDir = Paths.get("../liquidjava-example/src/main/java/testSuite/");

                    // 1. Files only in the root directory
                    boolean isFileInRootDir = fileAttr.isRegularFile() && filePath.getParent().equals(rootDir);

                    // 2. Directories and subdirectories (recursive search for directories)
                    boolean isDirectory = fileAttr.isDirectory();

                    // Return true if either condition matches
                    return isFileInRootDir || isDirectory;
                });
    }
}
