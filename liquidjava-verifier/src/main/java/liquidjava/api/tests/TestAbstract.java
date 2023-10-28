package liquidjava.api.tests;

import static org.junit.Assert.fail;

import liquidjava.api.CommandLineLauncher;
import liquidjava.errors.ErrorEmitter;

public abstract class TestAbstract {
    String testPath = "../liquidjava-example/src/test/java/liquidjava/";

    protected void testCorrect(String filename) {
        ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(filename);
        if (errorEmitter.foundError())
            fail();
    }

    protected void testWrong(String filename) {
        ErrorEmitter errorEmitter = CommandLineLauncher.launchTest(filename);
        if (!errorEmitter.foundError())
            fail();
    }
}
