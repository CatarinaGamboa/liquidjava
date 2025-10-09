package testSuite.classes.ambiguous_state_names_correct;

import liquidjava.specification.StateSet;
import liquidjava.specification.StateRefinement;

@StateSet({"open", "closed"})
public class Pipe {
    
    @StateRefinement(to = "open(this)")
    public Pipe() { }
}