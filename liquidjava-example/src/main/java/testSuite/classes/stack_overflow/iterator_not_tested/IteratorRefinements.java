package testSuite.classes.stack_overflow.iterator_not_tested;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;


@ExternalRefinementsFor("java.util.Iterator")
@StateSet({"start", "ready", "inNext"})
public interface IteratorRefinements {

    @StateRefinement(to = "start(this)")
    public void Iterator();

    @StateRefinement(to = "ready(this)")
    public boolean hasNext();

    @StateRefinement(from = "ready(this)", to = "inNext(this)")
    public Object next();

    @StateRefinement(from = "inNext(this)", to = "start(this)")
    public void remove();
}
