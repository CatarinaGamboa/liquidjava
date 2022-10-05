package repair.regen.processor.context;

import java.util.Optional;

import repair.regen.processor.constraints.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class VariableInstance extends RefinedVariable {

    // private Predicate state;
    private Variable parent;

    public VariableInstance(String name, CtTypeReference<?> type, Predicate c) {
        super(name, type, c);
        this.parent = null;
    }

    public VariableInstance(String name, CtTypeReference<?> type, Predicate c, Variable parent) {
        super(name, type, c);
        this.parent = parent;
    }

    @Override
    public Predicate getMainRefinement() {
        return super.getRefinement();
    }

    @Override
    public String toString() {
        return "VariableInstance [name=" + super.getName() + ", type=" + super.getType() + ", refinement="
                + super.getRefinement() + "]";
    }

    public void setParent(Variable p) {
        parent = p;
    }

    public Optional<Variable> getParent() {
        return parent == null ? Optional.empty() : Optional.of(parent);
    }

    // public void setState(Predicate c) {
    // state = c;
    // }
    // public Predicate getState() {
    // return state;
    // }
    //

}
