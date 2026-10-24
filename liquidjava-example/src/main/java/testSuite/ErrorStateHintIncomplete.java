package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
public class ErrorStateHintIncomplete {

    @StateRefinement(to = "open(this)")
    public ErrorStateHintIncomplete() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}

    // depends on its argument, so the hints cannot claim that closed is a final state
    @StateRefinement(from = "closed(this) && attempts > 0", to = "open(this)")
    public void reopen(int attempts) {}

    public static void main(String[] args) {
        ErrorStateHintIncomplete f = new ErrorStateHintIncomplete();
        f.close();
        f.read(); // Expect: State Refinement Error
    }
}
