package repair.regen;


public class ErrorGhostArgsTypes {
    @repair.regen.specification.RefinementPredicate("ghost boolean open(int)")
    @repair.regen.specification.Refinement("open(4.5) == true")
    public int one() {
        return 1;
    }
}

