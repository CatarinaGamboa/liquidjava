package repair.regen;


public class CorrectSearchIndexIntArray {
    public static void searchIndex(int[] l, @repair.regen.specification.Refinement("i >= 0")
    int i) {
        if (i >= (l.length))
            return;
        else {
            @repair.regen.specification.Refinement(" _ <= length(l)")
            int i2 = i + 1;
            repair.regen.CorrectSearchIndexIntArray.searchIndex(l, i2);
        }
    }
}

