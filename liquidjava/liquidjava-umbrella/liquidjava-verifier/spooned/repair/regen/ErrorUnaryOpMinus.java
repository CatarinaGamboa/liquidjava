package liquidjava;


public class ErrorUnaryOpMinus {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("b > 0")
        int b = 8;
        b = -b;
    }
}

