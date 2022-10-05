package regen.test.project;


@liquidjava.specification.ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
    @liquidjava.specification.Refinement("(a > b)? (_ == a):( _ == b)")
    public int max(int a, int b);
}

