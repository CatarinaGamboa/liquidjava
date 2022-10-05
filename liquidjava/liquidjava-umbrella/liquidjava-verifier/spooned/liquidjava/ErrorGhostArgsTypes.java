package liquidjava;


public class ErrorGhostArgsTypes {
    @liquidjava.specification.RefinementPredicate("ghost boolean open(int)")
    @liquidjava.specification.Refinement("open(4.5) == true")
    public int one() {
        return 1;
    }
}

