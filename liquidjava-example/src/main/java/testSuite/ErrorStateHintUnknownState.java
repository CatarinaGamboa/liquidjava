package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"created", "open", "closed"})
public class ErrorStateHintUnknownState {

    @StateRefinement(to = "created(this)")
    public ErrorStateHintUnknownState() {}

    @StateRefinement(from = "created(this)", to = "open(this)")
    public void open() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}

    public static void use(ErrorStateHintUnknownState f) {
        f.read(); // Expect: State Refinement Error
    }
}
