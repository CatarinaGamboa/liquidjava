package liquidjava;


public class ErrorGhostNumberArgs {
    @liquidjava.specification.RefinementPredicate("ghost boolean open(int)")
    @liquidjava.specification.Refinement("open(1,2) == true")
    public int one() {
        return 1;
    }
}

