package liquidjava.processor.context;

import java.util.List;
import liquidjava.rj_language.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class GhostState extends GhostFunction {

    private GhostFunction parent;
    private Predicate refinement;

    public GhostState(String name, List<CtTypeReference<?>> list, CtTypeReference<?> returnType, String prefix) {
        super(name, list, returnType, prefix);
    }

    public void setGhostParent(GhostFunction parent) {
        this.parent = parent;
    }

    public void setRefinement(Predicate c) {
        refinement = c;
    }

    public GhostFunction getParent() {
        return parent;
    }

    public Predicate getRefinement() {
        return refinement;
    }
}
