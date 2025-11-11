package liquidjava.api.tests;

import static org.junit.Assert.fail;

import liquidjava.api.CommandLineLauncher;
import liquidjava.errors.ErrorEmitter;
import org.junit.Test;

/**
 * Este teste chama o CommandLineLauncher e verifica se o ErrorEmitter reporta corretamente os resultados para ficheiros
 * conhecidos.
 */
public class TesteIntegracao {

    /**
     * Testa a verificação de um ficheiro que se espera estar correto. Este teste falha se o ErrorEmitter encontrar um
     * erro de Refinement.
     */
    @Test
    public void testVerification_CorrectFile() {
        String correctFilePath = "../liquidjava-example/src/main/java/testSuite/SimpleTest.java";
        ErrorEmitter errorEmitter = CommandLineLauncher.launch(correctFilePath);

        if (errorEmitter.foundError()) {
            System.out.println(
                    "ERROR: O ficheiro " + correctFilePath + " deveria estar correto, mas foi encontrado um erro.");
            fail();
        }
    }

    /**
     * Testa a verificação de um ficheiro que se espera conter um erro. Este teste falha se o ErrorEmitter não encontrar
     * um erro de Refinement.
     */
    @Test
    public void testVerification_ErrorFile() {
        String errorFilePath = "../liquidjava-example/src/main/java/testSuite/ErrorArithmetic.java";

        ErrorEmitter errorEmitter = CommandLineLauncher.launch(errorFilePath);

        if (!errorEmitter.foundError()) {
            System.out.println(
                    "ERROR: O ficheiro " + errorFilePath + " deveria conter um erro, mas passou na verificação.");
            fail();
        }
    }

    /**
     * Testa se a execução do método principal de CommandLineLauncher sem argumentos arranca sem lançar exceções
     */
    @Test
    public void testMainWithNoArguments() {
        CommandLineLauncher.main(new String[] {});
    }

}
