package liquidjava;


public class CorrectSearchIndexIntArray {
    public static void searchIndex(@liquidjava.specification.Refinement("length(l) > 0")
    int[] l, @liquidjava.specification.Refinement("i >= 0")
    int i) {
        if (i >= (l.length))
            return;
        else {
            @liquidjava.specification.Refinement(" _ <= length(l)")
            int p = i + 1;
            liquidjava.CorrectSearchIndexIntArray.searchIndex(l, p);
        }
    }
}

