package repair.regen;


public class ErrorRecursion1 {
    @repair.regen.specification.Refinement(" _ == 0")
    public static int untilZero(@repair.regen.specification.Refinement("k >= 0")
    int k) {
        if (k == 1)
            return 0;
        else
            return repair.regen.ErrorRecursion1.untilZero((k - 1));

    }
}

