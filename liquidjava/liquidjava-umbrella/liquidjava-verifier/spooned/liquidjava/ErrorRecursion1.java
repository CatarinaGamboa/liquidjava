package liquidjava;


public class ErrorRecursion1 {
    @liquidjava.specification.Refinement(" _ == 0")
    public static int untilZero(@liquidjava.specification.Refinement("k >= 0")
    int k) {
        if (k == 1)
            return 0;
        else
            return liquidjava.ErrorRecursion1.untilZero((k - 1));

    }
}

