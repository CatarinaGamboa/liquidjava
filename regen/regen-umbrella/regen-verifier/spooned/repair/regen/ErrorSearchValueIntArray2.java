package repair.regen;


public class ErrorSearchValueIntArray2 {
    @repair.regen.specification.RefinementFunction("ghost int length(int[])")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ < length(l))")
    public static int getIndexWithValue(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0 && i < length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        if (i >= ((l.length) - 1))
            return -1;
        else
            return repair.regen.ErrorSearchValueIntArray2.getIndexWithValue(l, (i + 1), val);

    }

    public static void main(java.lang.String[] args) {
        int[] arr = new int[10];
        repair.regen.ErrorSearchValueIntArray2.getIndexWithValue(arr, arr.length, 1000);
    }
}
