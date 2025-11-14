package testingInProgress.diagnostics.errors;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
public class StateConflictError {
    
    @StateRefinement(to="open(this)")
    public StateConflictError() {}

    @StateRefinement(from="open(this) && closed(this)")
    public void close() {}
}
