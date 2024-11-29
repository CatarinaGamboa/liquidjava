package testSuite.classes.stack_overflow.iterator_not_tested;

import java.util.Iterator;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.Refinement;

@ExternalRefinementsFor("java.util.LinkedList")
public interface LinkedListRefinements<T> {

    @Refinement("init(_)")//? or just assume its correct
    public Iterator<T> iterator();

    
}
