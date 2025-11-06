package liquidjava.api;

import org.junit.jupiter.api.Test;
/**
*Testa se a execução do método principal de CommandLineLauncher sem *argumentos arranca sem lançar exceções
*/

class TestCommandLineLauncher {

    @Test
    void testMainWithNoArguments() {
        // Apenas executa o programa sem argumentos
        // Deve correr sem lançar exceções
        CommandLineLauncher.main(new String[] {});
    }
}
