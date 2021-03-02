package repair.regen;


public class ErrorImplementationSearchValueIntArray {
    @repair.regen.specification.RefinementPredicate("ghost int length(int[])")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ < length(l))")
    public static int getIndexWithValue(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0 && i < length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        // with or without -1
        if (i >= (l.length))
            return -1;
        else
            return repair.regen.ErrorImplementationSearchValueIntArray.getIndexWithValue(l, (i + 1), val);

    }
}

