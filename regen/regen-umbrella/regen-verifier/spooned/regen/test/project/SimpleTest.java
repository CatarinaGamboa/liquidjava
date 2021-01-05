package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{n >= 0} -> {\\v >= 0}")
    public int fibonnaci(int n) {
        if (n <= 2)
            return n;
        else
            return (fibonnaci((n - 1))) + (fibonnaci((n - 2)));

    }

    public static void main(java.lang.String[] args) {
        // @Refinement("\\v > 5")
        // int a = 10;
        // @Refinement("\\v > a")
        // int b = 11;
    }
}

