package testSuite;

import liquidjava.specification.Refinement;
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

    @Refinement("_ == 1")
    static int one() { return 1; }

    @Refinement("_ == (n > 0)")
    static boolean positive(int n) { return n > 0; }

    static void closeTwice(boolean b) {
        ErrorNestedIfDoor d = new ErrorNestedIfDoor();
        if (b) {
            if (current() == d) {
                d.close();
            }
        }
        d.close(); // Expect: State Refinement Error
    }

    static void closeTwiceWithCallArgument(boolean b) {
        ErrorNestedIfDoor d = new ErrorNestedIfDoor();
        if (b) {
            if (positive(one() + 1)) {
                d.close();
            }
        }
        d.close(); // Expect: State Refinement Error
    }
}
