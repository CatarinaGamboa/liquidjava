package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
    @repair.regen.specification.Refinement("(a > b)? (_ == a):( _ == b)")
    public int max(int a, int b);
}

