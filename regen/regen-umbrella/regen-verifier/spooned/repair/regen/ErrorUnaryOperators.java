package repair.regen;


public class ErrorUnaryOperators {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int v = 3;
        v--;
        @repair.regen.specification.Refinement("\\v >= 10")
        int s = 100;
        s--;
    }
}

