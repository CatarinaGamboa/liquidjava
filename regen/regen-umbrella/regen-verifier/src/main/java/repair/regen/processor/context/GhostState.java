package repair.regen.processor.context;

import java.util.List;

import repair.regen.rj_language.Predicate;
import spoon.reflect.reference.CtTypeReference;

public class GhostState extends GhostFunction {

    private GhostFunction parent;
    private Predicate refinement;

    public GhostState(String name, List<CtTypeReference<?>> list, CtTypeReference<?> return_type, String klass) {
        super(name, list, return_type, klass);
        // TODO Auto-generated constructor stub
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
