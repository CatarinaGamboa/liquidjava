

@repair.regen.specification.ExternalRefinementsFor("java.util.ArrayList")
public interface ListRefinements {
    @repair.regen.specification.Refinement("len( _ ) == 0")
    public void ArrayList();
}

