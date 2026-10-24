package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"empty", "full"})
class ErrorTernaryBox {
    @StateRefinement(to = "empty(this)")
    public ErrorTernaryBox() {}

    @StateRefinement(from = "empty(this)", to = "full(this)")
    public void fill() {}

    @StateRefinement(to = "return ? full(this) : empty(this)")
    public boolean isFull() {
        return false;
    }

    @StateRefinement(from = "full(this)")
    public int peek() {
        return 1;
    }
}

public class ErrorTernaryConditionInBranch {
    static int peekIfFull(ErrorTernaryBox box) {
        boolean full = box.isFull();
        return full ? 0 : box.peek(); // Expect: State Refinement Error
    }
}
