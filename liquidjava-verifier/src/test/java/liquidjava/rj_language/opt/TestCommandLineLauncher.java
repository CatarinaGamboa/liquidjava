package liquidjava.api;

import org.junit.jupiter.api.Test;
/**
*Testa se a execução do método principal de CommandLineLauncher sem *argumentos arranca sem lançar exceções
*/

class TestCommandLineLauncher {

    @Test
    void testMainWithNoArguments() {
        CommandLineLauncher.main(new String[] {});
    }
}
