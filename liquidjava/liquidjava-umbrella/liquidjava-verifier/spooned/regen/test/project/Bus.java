package regen.test.project;


public class Bus extends regen.test.project.Car {
    @liquidjava.specification.StateRefinement(to = "!isOpen(this)")
    public Bus() {
    }

    @java.lang.Override
    public void open() {
        // TODO Auto-generated method stub
    }

    @java.lang.Override
    public void close() {
        // TODO Auto-generated method stub
    }
}

