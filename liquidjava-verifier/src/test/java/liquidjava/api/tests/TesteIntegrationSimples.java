package liquidjava.api.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import liquidjava.api.CommandLineLauncher;
import liquidjava.errors.ErrorEmitter;
import org.junit.Test;

public class TesteIntegrationSimples {

    @Test
    public void testLaunchOnSimpleExample() {
        String path = "../liquidjava-example/src/main/java/testSuite/SimpleTest.java";

        ErrorEmitter ee = CommandLineLauncher.launch(path);
        assertNotNull("nao pode ser null", ee);
        assertFalse("não é esperado nenhum erro de verificacao", ee.foundError());
    }
}