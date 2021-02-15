package repair.regen;


public class ErrorGhostArgsTypes {
    @repair.regen.specification.RefinementFunction("ghost boolean open(int)")
    @repair.regen.specification.Refinement("open(4.5) == true")
    public int one() {
        return 1;
    }
}

