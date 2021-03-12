package repair.regen.classes.state_from_superclass_correct;


public class Bus extends repair.regen.classes.state_from_superclass_correct.Car {
    @repair.regen.specification.StateRefinement(to = "close(this)")
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

