package testSuite;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"idle", "ready", "running"})
public class ErrorStateHintTwoCalls {

    @StateRefinement(to = "idle(this)")
    public ErrorStateHintTwoCalls() {}

    @StateRefinement(from = "idle(this)", to = "ready(this)")
    public void prepare() {}

    @StateRefinement(from = "ready(this)", to = "running(this)")
    public void start() {}

    @StateRefinement(from = "running(this)")
    public void step() {}

    public static void main(String[] args) {
        ErrorStateHintTwoCalls r = new ErrorStateHintTwoCalls();
        r.step(); // Expect: State Refinement Error
    }
}
