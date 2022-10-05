package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.util.stream.IntStream")
public interface OtherRefinement {
    public java.util.stream.IntStream range(int a, @liquidjava.specification.Refinement("b > a")
    int b);
}

