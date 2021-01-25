package repair.regen;


public class CorrectInvocationFromMathLibrary {
    public static void main(java.lang.String[] args) {
        // @Refinement("\\v == -6") //TODO REVIEW
        // int e = -Math.abs(-d);
        // Math.random()
        @repair.regen.specification.Refinement("\\v >= 0")
        double c = java.lang.Math.random();
        @repair.regen.specification.Refinement("\\v < 0")
        double f = -(java.lang.Math.random());
        @repair.regen.specification.Refinement("true")
        double r1 = java.lang.Math.random();
        @repair.regen.specification.Refinement("m2 > 0")
        double r2 = r1 * 5;
        // Math.abs(...)
        @repair.regen.specification.Refinement("b > 0")
        int b = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("\\v == 6")
        int a = java.lang.Math.abs(6);
        @repair.regen.specification.Refinement("\\v > 4")
        int d = java.lang.Math.abs((-6));
        @repair.regen.specification.Refinement("\\v > 0")
        double a1 = java.lang.Math.abs(15.3);
        @repair.regen.specification.Refinement("\\v > 10")
        long b1 = java.lang.Math.abs((-13));
        @repair.regen.specification.Refinement("\\v > 10")
        float c1 = java.lang.Math.abs((-13.0F));
        @repair.regen.specification.Refinement("\\v > 4")
        int d1 = java.lang.Math.abs((-6));
        @repair.regen.specification.Refinement("\\v == -6")
        int e1 = -(java.lang.Math.abs((-d1)));
        @repair.regen.specification.Refinement("\\v == -6")
        int f1 = -(java.lang.Math.abs(e1));
        @repair.regen.specification.Refinement("\\v == -6")
        int f2 = -(java.lang.Math.abs(f1));
        // Constants
        @repair.regen.specification.Refinement("\\v > 3")
        double a2 = java.lang.Math.PI;
        @repair.regen.specification.Refinement("\\v > 2")
        double b2 = java.lang.Math.E;
        @repair.regen.specification.Refinement("\\v == 30")
        double radius = 30;
        @repair.regen.specification.Refinement("perimeter > 1")
        double perimeter = (2 * (java.lang.Math.PI)) * radius;
        // addExact(...)
        @repair.regen.specification.Refinement("\\v == 11")
        int a3 = java.lang.Math.addExact(5, 6);
        @repair.regen.specification.Refinement("\\v > 10")
        long b3 = java.lang.Math.addExact(5L, 6L);
        @repair.regen.specification.Refinement("\\v < 4")
        double a5 = java.lang.Math.acos(0.5);
        @repair.regen.specification.Refinement("\\v < 2")
        double a6 = java.lang.Math.asin(a5);
        // decrementExact
        @repair.regen.specification.Refinement("\\v > 5")
        int a7 = 10;
        @repair.regen.specification.Refinement("\\v > 4")
        int a8 = java.lang.Math.decrementExact(a7);
        @repair.regen.specification.Refinement("\\v == 9")
        int a9 = java.lang.Math.decrementExact(a7);
        @repair.regen.specification.Refinement("\\v > 4")
        long a10 = java.lang.Math.decrementExact(a7);
        @repair.regen.specification.Refinement("\\v == 9")
        long a11 = java.lang.Math.decrementExact(a7);
        // incrementExact
        @repair.regen.specification.Refinement("\\v > 6")
        int a12 = java.lang.Math.incrementExact(a7);
        @repair.regen.specification.Refinement("\\v == 11")
        int a13 = java.lang.Math.incrementExact(a7);
        @repair.regen.specification.Refinement("\\v > 5")
        int prim = 10;
        @repair.regen.specification.Refinement("\\v > 6")
        int seg = java.lang.Math.incrementExact(prim);
        @repair.regen.specification.Refinement("\\v == 12")
        int ter = java.lang.Math.incrementExact(seg);
        // max
        @repair.regen.specification.Refinement("\\v == 5")
        int m1 = java.lang.Math.max(4, 5);
        @repair.regen.specification.Refinement("\\v > 5")
        int m2 = java.lang.Math.max(100, m1);
        @repair.regen.specification.Refinement("\\v == 100")
        int m3 = java.lang.Math.max(100, m2);
        @repair.regen.specification.Refinement("\\v == -100")
        int m4 = java.lang.Math.max((-1000), (-m2));
    }
}

