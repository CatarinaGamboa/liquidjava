package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class ErrorAlias {
    @liquidjava.specification.Refinement("InRange( _, 10, 16)")
    public static int getNum() {
        return 14;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("InRange( _, 10, 15)")
        int j = liquidjava.ErrorAlias.getNum();
    }
}

