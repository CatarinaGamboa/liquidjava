package liquidjava.classes.state_from_superclass_correct;


@liquidjava.specification.StateSet({ "open", "close" })
public abstract class Car {
    // @RefinementPredicate("boolean isOpen(Car c)")
    @liquidjava.specification.StateRefinement(from = "close(this)", to = "open(this)")
    public abstract void open();

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public abstract void close();
}

