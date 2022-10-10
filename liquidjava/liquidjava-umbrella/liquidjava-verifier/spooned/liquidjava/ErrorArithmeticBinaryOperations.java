package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorArithmeticBinaryOperations {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 100")
        int y = 50;
        @liquidjava.specification.Refinement("_ > 0")
        int z = y - 51;
    }
}

