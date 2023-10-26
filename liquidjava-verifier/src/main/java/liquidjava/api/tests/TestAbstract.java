package liquidjava.api.tests;

import static org.junit.Assert.fail;

import java.security.Permission;
import liquidjava.api.CommandLineLauncher;

public abstract class TestAbstract {
    String testPath = "../liquidjava-example/src/test/java/liquidjava/";

    protected void testCorrect(String filename) {
        try {
            CommandLineLauncher.launchTest(filename);
        } catch (SecurityException e) {
            fail();
        }
    }

    protected void testWrong(String filename) {
        try {
            CommandLineLauncher.launchTest(filename);
        } catch (SecurityException e) {
            return;
        }
        fail();
    }

    class MySecurityManager extends SecurityManager {
        // Handles exit(1) when the refinements are not respected
        @Override
        public void checkExit(int status) {
            if (status == 1 || status == 2)
                throw new SecurityException("subtyping");
        }

        @Override
        public void checkPermission(Permission perm) {
            // Allow other activities by default
        }
    }
}
