package testSuite;

import liquidjava.specification.Refinement;
import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
class RepeatedConditionDoor {
    @StateRefinement(to = "open(this)")
    public RepeatedConditionDoor() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}
}

public class CorrectRepeatedParameterCondition {
    @Refinement("_ == b")
    static boolean identity(boolean b) {
        if (b == true) {
            return b;
        }
        return b;
    }

    @Refinement("_ == b")
    static boolean returnAfterReassignment(boolean b) {
        if (b) {
            // Exercise the condition snapshot before the reassignment.
        }
        b = false;
        return b;
    }

    static void closeOnce(boolean b) {
        RepeatedConditionDoor d = new RepeatedConditionDoor();
        if (b == true) {
            d.close();
        }
        if (b == false) {
            d.close();
        }
        b = false;
    }
}
