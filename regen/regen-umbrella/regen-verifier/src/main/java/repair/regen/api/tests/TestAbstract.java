package repair.regen.api.tests;

import static org.junit.Assert.fail;

import java.security.Permission;

import repair.regen.api.CommandLineLauncher;

public abstract class TestAbstract {
    String testPath = "../regen-example/src/test/java/repair/regen/";

    protected void testCorrect(String filename) {
        MySecurityManager secManager = new MySecurityManager();
        System.setSecurityManager(secManager);
        try {
            CommandLineLauncher.launchTest(filename);
        } catch (SecurityException e) {
            fail();
        }
    }

    protected void testWrong(String filename) {
        MySecurityManager secManager = new MySecurityManager();
        System.setSecurityManager(secManager);
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
