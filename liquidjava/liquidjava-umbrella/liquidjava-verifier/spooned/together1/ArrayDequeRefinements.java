package together1;


@liquidjava.specification.ExternalRefinementsFor("java.util.ArrayDeque")
@liquidjava.specification.Ghost("int size")
public interface ArrayDequeRefinements<E> {
    public void ArrayDeque();

    @liquidjava.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean add(E elem);

    @liquidjava.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean offerFirst(E elem);

    @liquidjava.specification.StateRefinement(from = "size(this) > 0", to = "size(this) == (size(old(this)))")
    public E getFirst();

    @liquidjava.specification.StateRefinement(from = "size(this) > 0", to = "size(this) == (size(old(this)))")
    public E getLast();

    @liquidjava.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public void remove();

    @liquidjava.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public E pop();

    @liquidjava.specification.Refinement("_ == size(this)")
    public int size();

    @liquidjava.specification.Refinement("_ == (size(this) <= 0)")
    public boolean isEmpty();
}

