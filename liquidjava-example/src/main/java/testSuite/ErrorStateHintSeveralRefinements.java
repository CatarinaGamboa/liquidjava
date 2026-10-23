package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"created", "open", "closed"})
public class ErrorStateHintSeveralRefinements {

    @StateRefinement(to = "created(this)")
    public ErrorStateHintSeveralRefinements() {}

    @StateRefinement(from = "created(this)", to = "open(this)")
    public void open() {}

    // allowed in created and in open, but by different refinements, so not when the object can be in either
    @StateRefinement(from = "created(this)", to = "open(this)")
    @StateRefinement(from = "open(this)", to = "open(this)")
    public void ensureOpen() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    public static void use(boolean ready) {
        ErrorStateHintSeveralRefinements f = new ErrorStateHintSeveralRefinements();
        if (ready)
            f.open();
        f.read(); // Expect: State Refinement Error
    }
}
