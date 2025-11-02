package liquidjava.rj_language.opt;

import liquidjava.rj_language.Predicate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import liquidjava.errors.ErrorEmitter;

public class TestPredicate {

    @Test
    public void TestChangeOldMentions() {
        String oldName = ("Nome Velho");
        String newName = ("Nome Novo");
        ErrorEmitter ee = new ErrorEmitter();
        Predicate p = Predicate.createVar(oldName);
        Predicate result = p.changeOldMentions(oldName, newName, ee);
        assertEquals(newName, result.getExpression().toString());
    }
}
