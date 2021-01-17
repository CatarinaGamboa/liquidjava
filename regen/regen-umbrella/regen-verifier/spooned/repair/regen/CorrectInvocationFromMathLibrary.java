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
        // @Refinement("\\v == -6") //TODO REVIEW
        // int e = -Math.abs(-d);
        @repair.regen.specification.Refinement("\\v < 0")
        double f = -(java.lang.Math.random());
        @repair.regen.specification.Refinement("\\v > 0")
        double a1 = java.lang.Math.abs(15.3);
        @repair.regen.specification.Refinement("\\v > 10")
        long b1 = java.lang.Math.abs((-13));
        @repair.regen.specification.Refinement("\\v > 10")
        float c1 = java.lang.Math.abs((-13.0F));
        @repair.regen.specification.Refinement("\\v > 3")
        double a2 = java.lang.Math.PI;
        @repair.regen.specification.Refinement("\\v > 2")
        double b2 = java.lang.Math.E;
        @repair.regen.specification.Refinement("\\v == 30")
        double radius = 30;
        @repair.regen.specification.Refinement("perimeter > 1")
        double perimeter = (2 * (java.lang.Math.PI)) * radius;
    }
}

