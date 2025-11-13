package testSuite;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.RefinementPredicate;
import liquidjava.specification.StateRefinement;

@ExternalRefinementsFor("java.util.ArrayList")
public interface WarningExtRefWrongConstructor<E> {

    @RefinementPredicate("int size(ArrayList l)")
    @StateRefinement(to = "size(this) == 0")
    public void ArrayList(String wrongParameter);

    @StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean add(E e);
}
