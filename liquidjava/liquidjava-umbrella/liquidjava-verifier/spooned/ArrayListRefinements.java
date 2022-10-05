

@liquidjava.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ArrayListRefinements<E> {
    @liquidjava.specification.RefinementPredicate("int lengthA(ArrayList l)")
    @liquidjava.specification.Refinement("lengthA(this) == 0")
    public void ArrayList();

    // ?
    @liquidjava.specification.Refinement("lengthA(this) == (lengthA(old(this)) + 1)")
    public void add(E e);

    @liquidjava.specification.Refinement("lengthA(_) == lengthA(this)")
    public java.lang.Object clone();
}

