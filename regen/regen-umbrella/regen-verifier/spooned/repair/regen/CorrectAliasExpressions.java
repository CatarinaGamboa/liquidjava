package repair.regen;


@repair.regen.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class CorrectAliasExpressions {
    @repair.regen.specification.Refinement("InRange( _, 10, 16)")
    public static int getNum() {
        return 15;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("InRange( _, 10, 122+5)")
        int j = repair.regen.CorrectAliasExpressions.getNum();
    }
}

