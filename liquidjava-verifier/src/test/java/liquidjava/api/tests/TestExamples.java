package liquidjava.api.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import liquidjava.api.CommandLineLauncher;
import liquidjava.diagnostics.ErrorEmitter;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestExamples {

    /**
     * Test the file at the given path by launching the verifier and checking for errors. The file/directory is expected
     * to be either correct or contain an error based on its name.
     *
     * @param filePath
     *            path to the file to test
     *
     * @throws IOException
     *             if an I/O error occurs reading the test file
     */
    @ParameterizedTest
    @MethodSource("fileNameSource")
    public void testFile(final Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        boolean isErrorFile = fileName.startsWith("Error") || fileName.contains("error");
        boolean isCorrectFile = fileName.startsWith("Correct") || fileName.contains("correct");

        // Run the verifier on the file or package
        ErrorEmitter errorEmitter = CommandLineLauncher.launch(filePath.toAbsolutePath().toString());

        if (isCorrectFile) {
            // A "Correct" file should NOT have an error
            if (errorEmitter.foundError()) {
                System.out.println("Error in directory: " + fileName + " --- should be correct but an error was found");
                System.out.println("Error: " + errorEmitter.getTitleMessage() + " - " + errorEmitter.getFullMessage());
                fail();
            }
        } else if (isErrorFile) {
            // An "Error" file SHOULD have an error
            if (!errorEmitter.foundError()) {
                System.out
                        .println("Error in directory: " + fileName + " --- should be an error but passed verification");
                fail();
            } else {
                // NEW: Check if it's the *correct* error
                String expectedError = getExpectedError(filePath);

                // If an expected error is specified in the file, check it.
                // We check the 'title' for a match.
                if (expectedError != null) {
                    String actualErrorTitle = errorEmitter.getTitleMessage(); //
                    if (actualErrorTitle == null || !actualErrorTitle.equals(expectedError)) {
                        System.out.println("Error in directory: " + fileName + " --- wrong error message found.");
                        System.out.println("  Expected: " + expectedError);
                        System.out.println("  Actual: " + (actualErrorTitle != null ? actualErrorTitle : "NULL"));
                        fail();
                    }
                }
            }
        }
        // If it's neither "Correct" nor "Error", no assertions are made.
    }

    /**
     * Returns a Stream of paths to test files in the testSuite directory. These include files with names starting with
     * "Correct" or "Error", and directories containing "correct" or "error".
     *
     * @return Stream of paths to test files
     *
     * @throws IOException
     *             if an I/O error occurs or the path does not exist
     */
    private static Stream<Path> fileNameSource() throws IOException {
        return Files.find(Paths.get("../liquidjava-example/src/main/java/testSuite/"), Integer.MAX_VALUE,
                (filePath, fileAttr) -> {
                    String name = filePath.getFileName().toString();
                    // 1. Files that start with "Correct" or "Error"
                    boolean isFileStartingWithCorrectOrError = fileAttr.isRegularFile()
                            && (name.startsWith("Correct") || name.startsWith("Error"));

                    // 2. Folders (directories) that contain "correct" or "error"
                    boolean isDirectoryWithCorrectOrError = fileAttr.isDirectory()
                            && (name.contains("correct") || name.contains("error"));

                    // Return true if either condition matches
                    return isFileStartingWithCorrectOrError || isDirectoryWithCorrectOrError;
                });
    }

    /**
     * Test multiple paths at once, including both files and directories. This test ensures that the verifier can handle
     * multiple inputs correctly and that no errors are found in files/directories that are expected to be correct.
     */
    @Test
    public void testMultiplePaths() {
        String[] paths = { "../liquidjava-example/src/main/java/testSuite/SimpleTest.java",
                "../liquidjava-example/src/main/java/testSuite/classes/arraylist_correct", };
        ErrorEmitter errorEmitter = CommandLineLauncher.launch(paths);

        // Check if any of the paths that should be correct found an error
        if (errorEmitter.foundError()) {
            System.out.println("Error found in files that should be correct.");
            fail();
        }
    }

    /**
     * Reads the given file to find an expected error message specified in a comment. The comment format is: //
     * 
     * @ExpectedError: "Error Title"
     *
     * @param filePath
     *            path to the test file
     *
     * @return The expected error title, or null if not specified.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    private String getExpectedError(Path filePath) throws IOException {
        if (Files.isDirectory(filePath)) {
            // Currently, we don't support expected errors for entire directories.
            return null;
        }

        // Try to find the expected error comment in the first 10 lines
        try (Stream<String> lines = Files.lines(filePath).limit(10)) {
            return lines.map(String::trim).filter(line -> line.startsWith("// @ExpectedError:")).findFirst()
                    .map(line -> line.substring(line.indexOf(":") + 1).trim()) // Get text after the colon
                    .map(line -> line.replace("\"", "")) // Remove quotes
                    .orElse(null); // No expected error specified
        }
    }
}
