package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 4")
        int d = java.lang.Math.abs((-6));
        // TODO REVIEW
        @repair.regen.specification.Refinement("\\v == -6")
        int e = -(java.lang.Math.abs((-d)));
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

