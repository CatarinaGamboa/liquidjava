package liquidjava;


public class ErrorRandom {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("true")
        double m1 = java.lang.Math.random();
        @liquidjava.specification.Refinement("m2 <= 0")
        double m2 = m1 * 5;
    }
}

