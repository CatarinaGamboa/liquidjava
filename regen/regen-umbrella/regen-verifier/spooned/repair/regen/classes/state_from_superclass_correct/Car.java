package repair.regen.classes.state_from_superclass_correct;


@repair.regen.specification.StateSet({ "open", "close" })
public abstract class Car {
    // @RefinementPredicate("boolean isOpen(Car c)")
    @repair.regen.specification.StateRefinement(from = "close(this)", to = "open(this)")
    public abstract void open();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public abstract void close();
}

