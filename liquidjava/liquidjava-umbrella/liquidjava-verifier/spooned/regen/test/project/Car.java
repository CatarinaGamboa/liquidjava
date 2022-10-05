package regen.test.project;


public abstract class Car {
    @liquidjava.specification.RefinementPredicate("boolean isOpen(Car c)")
    @liquidjava.specification.StateRefinement(from = "!isOpen(this)", to = "isOpen(this)")
    public abstract void open();

    @liquidjava.specification.StateRefinement(from = "isOpen(this)", to = "!isOpen(this)")
    public abstract void close();
}

