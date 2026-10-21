package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
public class ErrorStateHintFinalState {

    @StateRefinement(to = "open(this)")
    public ErrorStateHintFinalState() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}

    public static void main(String[] args) {
        ErrorStateHintFinalState f = new ErrorStateHintFinalState();
        f.close();
        f.read(); // Expect: State Refinement Error
    }
}
