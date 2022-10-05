

@liquidjava.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ListRefinements {
    @liquidjava.specification.Refinement("len( _ ) == 0")
    public void ArrayList();
}

