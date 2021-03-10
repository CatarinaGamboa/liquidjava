

@repair.regen.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @repair.regen.specification.RefinementPredicate("int lengthA(ArrayList l)")
    @repair.regen.specification.Refinement("lengthA(this) == 0")
    public void ArrayList();

    // ?
    @repair.regen.specification.Refinement("lengthA(this) == (lengthA(old(this)) + 1)")
    public void add(E e);

    @repair.regen.specification.Refinement("lengthA(_) == lengthA(this)")
    public java.lang.Object clone();
}

