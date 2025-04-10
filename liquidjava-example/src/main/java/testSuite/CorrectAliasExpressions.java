package testSuite;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

@RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class CorrectAliasExpressions {

    @Refinement("InRange( _, 10, 16)")
    public static int getNum() {
        return 15;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        @Refinement("InRange( _, 10, 122+5)")
        int j = getNum();
    }
}
