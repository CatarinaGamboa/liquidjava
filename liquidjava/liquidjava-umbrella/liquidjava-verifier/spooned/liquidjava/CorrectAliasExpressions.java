package liquidjava;


@liquidjava.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class CorrectAliasExpressions {
    @liquidjava.specification.Refinement("InRange( _, 10, 16)")
    public static int getNum() {
        return 15;
    }

    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("InRange( _, 10, 122+5)")
        int j = liquidjava.CorrectAliasExpressions.getNum();
    }
}
