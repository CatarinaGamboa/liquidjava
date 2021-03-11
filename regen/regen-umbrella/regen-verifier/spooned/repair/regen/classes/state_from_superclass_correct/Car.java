package repair.regen.classes.state_from_superclass_correct;


public abstract class Car {
    @repair.regen.specification.RefinementPredicate("boolean isOpen(Car c)")
    @repair.regen.specification.StateRefinement(from = "!isOpen(this)", to = "isOpen(this)")
    public abstract void open();

    @repair.regen.specification.StateRefinement(from = "isOpen(this)", to = "!isOpen(this)")
    public abstract void close();
}

