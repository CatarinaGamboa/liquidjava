package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a == 11")
        int a = java.lang.Math.addExact(5, 6);
        @repair.regen.specification.Refinement("b > 10")
        long b = java.lang.Math.addExact(5L, 6L);
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

