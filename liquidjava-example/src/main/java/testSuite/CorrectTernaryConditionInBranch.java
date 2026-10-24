package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"empty", "full"})
class CorrectTernaryBox {
    @StateRefinement(to = "empty(this)")
    public CorrectTernaryBox() {}

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

public class CorrectTernaryConditionInBranch {
    static int peekIfFull(CorrectTernaryBox box) {
        boolean full = box.isFull();
        return full ? box.peek() : 0;
    }
}
