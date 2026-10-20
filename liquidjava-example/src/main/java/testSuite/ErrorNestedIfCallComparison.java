package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
class ErrorNestedIfDoor {
    @StateRefinement(to = "open(this)")
    public ErrorNestedIfDoor() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}
}

public class ErrorNestedIfCallComparison {
    static ErrorNestedIfDoor current() {
        return new ErrorNestedIfDoor();
    }

    static void closeTwice(boolean b) {
        ErrorNestedIfDoor d = new ErrorNestedIfDoor();
        if (b) {
            if (current() == d) {
                d.close();
            }
        }
        d.close(); // Expect: State Refinement Error
    }
}
