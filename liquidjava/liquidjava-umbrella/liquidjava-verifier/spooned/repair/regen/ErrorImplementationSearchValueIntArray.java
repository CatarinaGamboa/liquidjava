package liquidjava;


public class ErrorImplementationSearchValueIntArray {
    @liquidjava.specification.RefinementPredicate("ghost int length(int[])")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ < length(l))")
    public static int getIndexWithValue(@liquidjava.specification.Refinement("length(l) > 0")
    int[] l, @liquidjava.specification.Refinement("i >= 0 && i < length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        // with or without -1
        if (i >= (l.length))
            return -1;
        else
            return liquidjava.ErrorImplementationSearchValueIntArray.getIndexWithValue(l, (i + 1), val);

    }
}

