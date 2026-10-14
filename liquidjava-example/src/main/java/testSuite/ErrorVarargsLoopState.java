package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "marked"})
public class ErrorVarargsLoopState {
    @StateRefinement(to = "open(this)")
    ErrorVarargsLoopState() {}

    @StateRefinement(to = "marked(this)")
    void mark(int... tags) {}

    @StateRefinement(from = "marked(this)", to = "open(this)")
    void reset() {}

    // The loop may run zero times, leaving b open, even when mark spreads its varargs.
    static void mayBeEmpty(int n) {
        ErrorVarargsLoopState b = new ErrorVarargsLoopState();
        int k = 0;
        while (k < n) {
            b.mark(1, 2);
            k++;
        }
        b.reset(); // Expect: State Refinement Error
    }
}
