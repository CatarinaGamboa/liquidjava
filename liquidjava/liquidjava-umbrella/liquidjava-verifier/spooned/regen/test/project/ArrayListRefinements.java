package regen.test.project;


// @Refinement("size(_) == size(this)")
// public Object clone();
@liquidjava.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @liquidjava.specification.RefinementPredicate("int size(ArrayList l)")
    @liquidjava.specification.Refinement("size(this) == 0")
    public void ArrayList();

    @liquidjava.specification.Refinement("size(this) == (size(old(this)) + 1)")
    public void add(E e);

    public E get(@liquidjava.specification.Refinement("index >= 0 && index < size(this)")
    int index);
}

