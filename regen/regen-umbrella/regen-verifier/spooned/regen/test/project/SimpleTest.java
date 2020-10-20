package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    public static void main(java.lang.String[] args) {
        // @Refinement("\\v <= 10")
        // int a = 1;
        @repair.regen.specification.Refinement("\\v < 10")
        int v = 3;
        v--;
        @repair.regen.specification.Refinement("\\v >= 10")
        int s = 100;
        s--;
    }
}

