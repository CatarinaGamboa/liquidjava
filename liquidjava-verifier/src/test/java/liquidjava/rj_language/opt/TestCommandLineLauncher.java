package liquidjava.api;

import org.junit.jupiter.api.Test;

class TestCommandLineLauncher {

    @Test
    void testMainWithNoArguments() {
        // Apenas executa o programa sem argumentos
        // Deve correr sem lançar exceções
        CommandLineLauncher.main(new String[] {});
    }
}
