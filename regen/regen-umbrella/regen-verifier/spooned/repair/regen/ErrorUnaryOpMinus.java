package repair.regen;


public class ErrorUnaryOpMinus {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("b > 0")
        int b = 8;
        b = -b;
    }
}

