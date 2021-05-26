package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.util.PriorityQueue")
@repair.regen.specification.Ghost("int size")
public interface PriorityQueueRefinements<E> {
    public void PriorityQueue();

    @repair.regen.specification.StateRefinement(to = "size(this) == (size(old(this)) + 1)")
    public boolean add(E elem);

    @repair.regen.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public void remove();

    @repair.regen.specification.StateRefinement(from = "size(this)> 0", to = "size(this) == (size(old(this)) - 1)")
    public E poll();
}

