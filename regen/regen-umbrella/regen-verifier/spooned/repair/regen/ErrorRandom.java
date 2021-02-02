package repair.regen;


public class ErrorRandom {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("true")
        double m1 = java.lang.Math.random();
        @repair.regen.specification.Refinement("m2 <= 0")
        double m2 = m1 * 5;
    }
}

