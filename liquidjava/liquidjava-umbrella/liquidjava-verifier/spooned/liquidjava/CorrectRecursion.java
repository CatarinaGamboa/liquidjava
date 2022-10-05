package liquidjava;


public class CorrectRecursion {
    @liquidjava.specification.Refinement("_ == 0")
    public static int untilZero(@liquidjava.specification.Refinement("k >= 0")
    int k) {
        if (k == 0)
            return 0;
        else
            return liquidjava.CorrectRecursion.untilZero((k - 1));

    }
}

