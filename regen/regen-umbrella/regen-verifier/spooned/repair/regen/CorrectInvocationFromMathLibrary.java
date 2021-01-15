package repair.regen;


public class CorrectInvocationFromMathLibrary {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("b > 0")
        int b = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("\\v >= 0")
        double c = java.lang.Math.random();
        @repair.regen.specification.Refinement("\\v == 6")
        int a = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("\\v > 4")
        int d = java.lang.Math.abs((-6));
        @repair.regen.specification.Refinement("\\v == -6")
        int e = -(java.lang.Math.abs((-d)));
        @repair.regen.specification.Refinement("\\v < 0")
        double f = -(java.lang.Math.random());
        @repair.regen.specification.Refinement("\\v > 0")
        double a1 = java.lang.Math.abs(15.3);
        @repair.regen.specification.Refinement("\\v > 10")
        long b1 = java.lang.Math.abs((-13));
        @repair.regen.specification.Refinement("\\v > 10")
        float c1 = java.lang.Math.abs((-13.0F));
    }
}

