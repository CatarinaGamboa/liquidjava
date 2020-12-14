package repair.regen;


public class ErrorRecursion1 {
    @repair.regen.specification.Refinement("{k >= 0}->{\\v == 0}")
    public static int untilZero(int k) {
        if (k == 1)
            return 0;
        else
            return repair.regen.ErrorRecursion1.untilZero((k - 1));

    }
}

