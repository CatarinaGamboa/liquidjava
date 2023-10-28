package liquidjava.api.tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import liquidjava.api.CommandLineLauncher;
import liquidjava.errors.ErrorEmitter;

public class TestExamples {

    @Test
    public void testFile() {
        try {
            Files.find(Paths.get("../liquidjava-example/src/main/java/"), Integer.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach(f -> {
                        ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(f.toAbsolutePath().toString());
                        if (f.startsWith("Correct") && errorEmitter.foundError()) {
                            fail();
                        }
                        if (f.startsWith("Error") && !errorEmitter.foundError()) {
                            fail();
                        }
                    });
        } catch (IOException e) {
            assert false;
        }
    }
}
