package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
class CorrectNestedIfDoor {
    @StateRefinement(to = "open(this)")
    public CorrectNestedIfDoor() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}
}

public class CorrectNestedIfCallComparison {
    static CorrectNestedIfDoor current() {
        return new CorrectNestedIfDoor();
    }

    static void compareInNestedIf(boolean b) {
        CorrectNestedIfDoor d = new CorrectNestedIfDoor();
        if (b) {
            if (current() == d) {
                System.out.println("same");
            }
        }
        d.close();
    }
}
