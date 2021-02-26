package repair.regen;


@repair.regen.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class ErrorAlias {
    @repair.regen.specification.Refinement("InRange( _, 10, 16)")
    public static int getNum() {
        return 14;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("InRange( _, 10, 15)")
        int j = repair.regen.ErrorAlias.getNum();
    }
}

