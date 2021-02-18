package repair.regen;


public class ErrorSearchIntArray {
    public static void searchIndex(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0 && i <= length(l)")
    int i) {
        if (i > (l.length))
            return;
        else
            repair.regen.ErrorSearchIntArray.searchIndex(l, (i + 1));

    }
}

