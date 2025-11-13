package liquidjava.api.tests;

import static org.junit.Assert.fail;

import liquidjava.api.CommandLineLauncher;
import org.junit.Test;

    /**
     * Testa se a execução do método principal de CommandLineLauncher sem argumentos arranca sem lançar exceções
     */
    @Test
    public void testMainWithNoArguments() {
        CommandLineLauncher.main(new String[] {});
    }

}
