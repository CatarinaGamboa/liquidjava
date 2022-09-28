package repair.regen.processor.constraints;

import repair.regen.ast.LiteralInt;

public class LiteralPredicate {

    public static Constraint getIntPredicate(int i) {
        LiteralInt li = new LiteralInt(i);
        return new Predicate(li);
    }

    // TODO COMPLETE
}
