package repair.regen;


public class CorrectRecursion {
    @repair.regen.specification.Refinement("{k >= 0}->{_ == 0}")
    public static int untilZero(int k) {
        if (k == 0)
            return 0;
        else
            return repair.regen.CorrectRecursion.untilZero((k - 1));

    }
}

