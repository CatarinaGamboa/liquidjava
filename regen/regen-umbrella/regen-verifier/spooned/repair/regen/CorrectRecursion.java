package repair.regen;


public class CorrectRecursion {
    @repair.regen.specification.Refinement("_ == 0")
    public static int untilZero(@repair.regen.specification.Refinement("k >= 0")
    int k) {
        if (k == 0)
            return 0;
        else
            return repair.regen.CorrectRecursion.untilZero((k - 1));

    }
}

