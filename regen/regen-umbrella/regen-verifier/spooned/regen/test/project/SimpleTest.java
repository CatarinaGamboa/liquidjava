package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 0")
        double a = java.lang.Math.abs(15.3);
        @repair.regen.specification.Refinement("\\v > 10")
        long b = java.lang.Math.abs((-13));
        @repair.regen.specification.Refinement("\\v > 10")
        float c = java.lang.Math.abs((-13.0F));
        // @Refinement("\\v > 0")
        // double e = Math.sqrt(6);
        // @Refinement("\\v >= 0")
        // double c = Math.random();
        // @Refinement("b > 0")
        // int b = Math.addExact(6, 2);
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

