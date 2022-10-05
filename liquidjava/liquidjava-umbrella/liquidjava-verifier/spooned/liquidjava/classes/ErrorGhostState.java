package liquidjava.classes;


// Should have no parameters
@liquidjava.specification.StateSet({ "empty", "addingItems", "checkout", "closed" })
@liquidjava.specification.Ghost("int totalPrice(int x)")
public class ErrorGhostState {
    @liquidjava.specification.StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
    public ErrorGhostState() {
    }
}

