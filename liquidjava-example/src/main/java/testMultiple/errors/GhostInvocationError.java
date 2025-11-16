package testMultiple.errors;

import liquidjava.specification.Ghost;
import liquidjava.specification.StateRefinement;

@Ghost("int size")
public class GhostInvocationError {

    @StateRefinement(to="size(this, this) == 0")
    public void test() {}
}
