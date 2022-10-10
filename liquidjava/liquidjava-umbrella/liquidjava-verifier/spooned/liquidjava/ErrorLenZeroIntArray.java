package liquidjava;


public class ErrorLenZeroIntArray {
    public static int getIndexWithVal(@liquidjava.specification.Refinement("length(l) > 0")
    int[] l, @liquidjava.specification.Refinement("i >= 0 && i <= length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        if (i >= (l.length))
            return -1;
        else
            return liquidjava.ErrorLenZeroIntArray.getIndexWithVal(l, (i + 1), val);

    }

    public static void main(java.lang.String[] args) {
        int[] a = new int[0];
        liquidjava.ErrorLenZeroIntArray.getIndexWithVal(a, 0, 6);
    }
}

