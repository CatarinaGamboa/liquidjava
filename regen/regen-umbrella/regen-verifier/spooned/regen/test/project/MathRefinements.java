package regen.test.project;


@repair.regen.specification.ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
    @repair.regen.specification.Refinement("_ == 3.141592653589793")
    public double PI = 0;

    @repair.regen.specification.Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
    public int abs(int arg0);
}

