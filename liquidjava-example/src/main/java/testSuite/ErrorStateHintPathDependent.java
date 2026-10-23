package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"created", "open", "closed"})
public class ErrorStateHintPathDependent {

    @StateRefinement(to = "created(this)")
    public ErrorStateHintPathDependent() {}

    @StateRefinement(from = "created(this)", to = "open(this)")
    public void open() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    @StateRefinement(from = "open(this)", to = "closed(this)")
    public void close() {}

    public static void use(boolean ready) {
        ErrorStateHintPathDependent f = new ErrorStateHintPathDependent();
        if (ready)
            f.open();
        f.read(); // Expect: State Refinement Error
    }
}
