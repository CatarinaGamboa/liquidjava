package bufferedreader;


// @Refinement("size(this) == (size(old(this)) - 1)")
// public void remove(int index);
// public E get(@Refinement("index >= 0 && index < size(this)") int index);
// @Refinement("size(_) == size(this)")
// public Object clone();
@repair.regen.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @repair.regen.specification.RefinementPredicate("int size(ArrayList l)")
    @repair.regen.specification.Refinement("size(this) == 0")
    public void ArrayList();

    @repair.regen.specification.Refinement("size(this) == (size(old(this)) + 1)")
    public void add(E e);
}

