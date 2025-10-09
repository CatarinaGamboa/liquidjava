package testSuite.classes.ambiguous_state_names_correct;

import liquidjava.specification.Refinement;

public class SimpleTest {

    public static void main(String[] args) {
        Door d = new Door(); // contains 'open' and 'closed' states
        Pipe p = new Pipe(); // unrelated type with the same state names
        requiresOpen(d); // ok iff 'open' binds to Door.open otherwise it may bind to Pipe.open and fail
    }

    public static void requiresOpen(@Refinement("open(s)") Door s) { }
}