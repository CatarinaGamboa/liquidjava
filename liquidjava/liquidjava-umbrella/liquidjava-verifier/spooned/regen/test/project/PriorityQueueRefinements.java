package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.util.PriorityQueue")
@liquidjava.specification.Ghost("int size")
public interface PriorityQueueRefinements<E> {
    public void PriorityQueue();

    @liquidjava.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean add(E elem);

    @liquidjava.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public void remove();

    @liquidjava.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public E poll();
}

