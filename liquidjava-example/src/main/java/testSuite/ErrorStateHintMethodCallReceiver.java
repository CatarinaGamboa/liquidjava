package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"created", "open"})
public class ErrorStateHintMethodCallReceiver {

    @StateRefinement(to = "created(this)")
    public ErrorStateHintMethodCallReceiver() {}

    @StateRefinement(from = "created(this)", to = "open(this)")
    public void open() {}

    @StateRefinement(from = "open(this)")
    public void read() {}

    // a call added before the line would act on a different object than the one returned here
    public static void use() {
        new ErrorStateHintMethodCallReceiver().read(); // Expect: State Refinement Error
    }
}
