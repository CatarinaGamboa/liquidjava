package liquidjava;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // Arithmetic Binary Operations
        @liquidjava.specification.Refinement("a >= 10")
        int a = 10;
        @liquidjava.specification.Refinement("t > 0")
        int t = a + 1;
    }
}

