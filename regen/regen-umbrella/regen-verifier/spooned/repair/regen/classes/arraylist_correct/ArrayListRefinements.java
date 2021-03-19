package repair.regen.classes.arraylist_correct;


// @Refinement("size(_) == size(this)")
// public Object clone();
@repair.regen.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @repair.regen.specification.RefinementPredicate("int size(ArrayList l)")
    @repair.regen.specification.StateRefinement(to = "size(this) == 0")
    public void ArrayList();

    @repair.regen.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public void add(E e);
}

