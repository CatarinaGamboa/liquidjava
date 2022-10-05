package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectArithmeticBinaryOperations {
    public static void main(java.lang.String[] args) {
        // Arithmetic Binary Operations
        @liquidjava.specification.Refinement("a == 10")
        int a = 10;
        @liquidjava.specification.Refinement("b != 10")
        int b = 5;
        @liquidjava.specification.Refinement("t > 0")
        int t = a + 1;
        @liquidjava.specification.Refinement("_ >= 9")
        int k = a - 1;
        @liquidjava.specification.Refinement("_ >= 5")
        int l = k * t;
        @liquidjava.specification.Refinement("_ > 0")
        int m = l / 2;
    }
}

