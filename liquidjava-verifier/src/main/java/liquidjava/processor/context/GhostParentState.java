package liquidjava.processor.context;

import java.util.ArrayList;
import java.util.List;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class GhostParentState extends GhostFunction {

    private ArrayList<GhostState> states;

    public GhostParentState(String name, List<String> params, CtTypeReference<?> ret, Factory factory, String prefix) {
        super(name, params, ret, factory, prefix);
        states = new ArrayList<>();
    }

    public void addState(GhostState s) {
        states.add(s);
    }

    public GhostState getFirstState() {
        return states.get(0);
    }

    public ArrayList<GhostState> getStates() {
        return states;
    }
}
