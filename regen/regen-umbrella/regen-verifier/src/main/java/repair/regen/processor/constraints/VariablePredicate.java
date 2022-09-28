package repair.regen.processor.constraints;

import repair.regen.ast.Var;

public class VariablePredicate extends Predicate {
    public VariablePredicate(String name) {
        super();
        exp = new Var(name);
    }

}
