package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
class ErrorIfReferenceDoor {
    @StateRefinement(to = "open(this)")
    public ErrorIfReferenceDoor() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}
}

public class ErrorIfReferenceComparison {
    static void closeTwice(ErrorIfReferenceDoor other) {
        ErrorIfReferenceDoor d = new ErrorIfReferenceDoor();
        if (other == d) {
            d.close();
        }
        d.close(); // Expect: State Refinement Error
    }
}
