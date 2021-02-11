package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.util.stream.IntStream")
public interface OtherRefinement {
    public java.util.stream.IntStream range(int a, @repair.regen.specification.Refinement("b > a")
    int b);
}

