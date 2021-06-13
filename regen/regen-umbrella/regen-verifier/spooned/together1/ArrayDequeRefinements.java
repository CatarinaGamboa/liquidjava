package together1;


@repair.regen.specification.ExternalRefinementsFor("java.util.ArrayDeque")
@repair.regen.specification.Ghost("int size")
public interface ArrayDequeRefinements<E> {
    public void ArrayDeque();

    @repair.regen.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean add(E elem);

    @repair.regen.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean offerFirst(E elem);

    @repair.regen.specification.StateRefinement(from = "size(this) > 0", to = "size(this) == (size(old(this)))")
    public E getFirst();

    @repair.regen.specification.StateRefinement(from = "size(this) > 0", to = "size(this) == (size(old(this)))")
    public E getLast();

    @repair.regen.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public void remove();

    @repair.regen.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public E pop();

    @repair.regen.specification.Refinement("_ == size(this)")
    public int size();

    @repair.regen.specification.Refinement("_ == (size(this) <= 0)")
    public boolean isEmpty();
}

