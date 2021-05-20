package repair.regen.classes;


// Should have no parameters
@repair.regen.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
@repair.regen.specification.Ghost("int totalPrice(int x)")
public class ErrorGhostState {
    @repair.regen.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public ErrorGhostState() {
    }
}

