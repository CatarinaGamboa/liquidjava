package liquidjava.rj_language.ast;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VarBasicsTest {

    @Test
    void testeNome_Clone() {         
        Var v = new Var("x");                       //metodos similares retornam o mesmo

        assertEquals("x", v.getName(), "Nome var");
        assertEquals("x", v.toString(), "toString deve devolver o nome");

        Var vClone = (Var) v.clone();                    //mesmo objeto mas dif instancia
        assertNotSame(v, vClone, "clone deve ser outra instância");
        assertEquals(v, vClone, "clone deve ser igual ao original");
    }
}