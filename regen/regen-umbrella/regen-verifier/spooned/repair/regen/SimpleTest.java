package repair.regen;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // Arithmetic Binary Operations
        @repair.regen.specification.Refinement("a >= 10")
        int a = 10;
        @repair.regen.specification.Refinement("t > 0")
        int t = a + 1;
    }
}

