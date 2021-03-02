package repair.regen;


public class ErrorGhostNumberArgs {
    @repair.regen.specification.RefinementPredicate("ghost boolean open(int)")
    @repair.regen.specification.Refinement("open(1,2) == true")
    public int one() {
        return 1;
    }
}

