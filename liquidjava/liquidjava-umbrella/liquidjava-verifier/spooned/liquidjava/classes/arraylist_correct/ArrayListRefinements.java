package liquidjava.classes.arraylist_correct;


// @Refinement("size(_) == size(this)")
// public Object clone();
@liquidjava.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @liquidjava.specification.RefinementPredicate("int size(ArrayList l)")
    @liquidjava.specification.StateRefinement(to = "size(this) == 0")
    public void ArrayList();

    @liquidjava.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public void add(E e);
}

