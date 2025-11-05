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
     */
    @ParameterizedTest
    @MethodSource("fileNameSource")
    public void testFile(final Path filePath) {
        String fileName = filePath.getFileName().toString();

        // 1. Run the verifier on the file or package
        ErrorEmitter errorEmitter = CommandLineLauncher.launch(filePath.toAbsolutePath().toString());

        // 2. Check if the file is correct or contains an error
        if ((fileName.startsWith("Correct") && errorEmitter.foundError())
                || (fileName.contains("correct") && errorEmitter.foundError())) {
            System.out.println("Error in directory: " + fileName + " --- should be correct but an error was found");
            fail();
        }
        // 3. Check if the file has an error but passed verification
        if ((fileName.startsWith("Error") && !errorEmitter.foundError())
                || (fileName.contains("error") && !errorEmitter.foundError())) {
            System.out.println("Error in directory: " + fileName + " --- should be an error but passed verification");
            fail();
        }
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
}
