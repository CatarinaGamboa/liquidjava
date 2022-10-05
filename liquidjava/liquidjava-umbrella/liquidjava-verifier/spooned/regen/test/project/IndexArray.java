package regen.test.project;


public class IndexArray {
    @liquidjava.specification.RefinementFunction("ghost int length(int[])")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ < length(l))")
    public static int getIndexWithValue(@liquidjava.specification.Refinement("length(l) > 0")
    int[] l, @liquidjava.specification.Refinement("i >= 0 && i < length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        if (i >= ((l.length) - 1))
            return -1;
        else
            return regen.test.project.IndexArray.getIndexWithValue(l, (i + 1), val);

    }

    public static void main(java.lang.String[] args) {
        int[] arr = new int[10];
        regen.test.project.IndexArray.getIndexWithValue(arr, 0, 1000);
        // ghost function valor em array
    }
}

