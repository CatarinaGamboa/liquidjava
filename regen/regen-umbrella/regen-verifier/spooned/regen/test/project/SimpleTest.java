package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // SEE ERROR still error
        // @Refinement("(\\v == -5)")
        // float prim = Math.copySign(-5, -500);
        // @Refinement("\\v == -656")
        // float ter = Math.copySign(656, prim);
        @repair.regen.specification.Refinement("\\v < 0")
        int y1 = java.lang.Math.addExact(5, (-6));
        @repair.regen.specification.Refinement("\\v == -5")
        int a3 = java.lang.Math.addExact((-6), y1);
        // See error NaN
        // @Refinement("true")
        // double b = 0/0;
        // @Refinement("\\v > 5")
        // double c = b;
        // @Refinement("true")
        // int a = 10;
        // int b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

