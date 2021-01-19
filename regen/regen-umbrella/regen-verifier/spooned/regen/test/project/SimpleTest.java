package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // @Refinement("\\v < 4")
        // double a5 = Math.acos(0.5);
        @repair.regen.specification.Refinement("\\v < 2")
        double a6 = java.lang.Math.asin(0.5);
        @repair.regen.specification.Refinement("(\\v == -5)")
        float a7 = java.lang.Math.copySign((-5), (-500));
        @repair.regen.specification.Refinement("\\v == 5")
        float a8 = java.lang.Math.copySign((-5), 6);
        @repair.regen.specification.Refinement("\\v == -656")
        float a9 = java.lang.Math.copySign(656, a7);
        // See error NaN
        // @Refinement("\\v > 4")
        // int d = Math.abs(-6);
        // 
        // @Refinement("\\v == -6")
        // int e = -Math.abs(-d);
        // See error NaN
        // @Refinement("true")
        // double b = 0/0;
        // @Refinement("\\v > 5")
        // double c = b;
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

