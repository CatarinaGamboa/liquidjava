package testSuite.classes.index_out_of_bounds_default_this_correct;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.Ghost;
import liquidjava.specification.Refinement;
import liquidjava.specification.StateRefinement;

@ExternalRefinementsFor("java.util.ArrayList")
@Ghost("int size")
public interface ArrayListRefinements<E> {

    public void ArrayList();

	@StateRefinement(to = "size() == size(old()) + 1")
	public boolean add(E elem);

	public E get(@Refinement("_ < size()") int index);
}