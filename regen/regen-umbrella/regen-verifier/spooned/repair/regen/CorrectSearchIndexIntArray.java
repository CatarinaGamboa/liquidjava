package repair.regen;


public class CorrectSearchIndexIntArray {
    public static void searchIndex(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0")
    int i) {
        if (i >= (l.length))
            return;
        else {
            @repair.regen.specification.Refinement(" _ <= length(l)")
            int p = i + 1;
            repair.regen.CorrectSearchIndexIntArray.searchIndex(l, p);
        }
    }
}

