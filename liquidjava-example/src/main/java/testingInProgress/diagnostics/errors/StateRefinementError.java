package testingInProgress.diagnostics.errors;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
public class StateRefinementError {
    
    @StateRefinement(to="open(this)")
    public StateRefinementError() {}

    @StateRefinement(from="open(this)", to="closed(this)")
    public void close() {}

    public static void main(String[] args) {
        StateRefinementError s = new StateRefinementError();
        s.close();
        s.close();
    }
}
