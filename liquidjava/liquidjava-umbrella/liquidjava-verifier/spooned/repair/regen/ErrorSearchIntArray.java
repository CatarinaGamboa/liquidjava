package liquidjava;


public class ErrorSearchIntArray {
    public static void searchIndex(@liquidjava.specification.Refinement("length(l) > 0")
    int[] l, @liquidjava.specification.Refinement("i >= 0 && i <= length(l)")
    int i) {
        if (i > (l.length))
            return;
        else
            liquidjava.ErrorSearchIntArray.searchIndex(l, (i + 1));

    }
}

