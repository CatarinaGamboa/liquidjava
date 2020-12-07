package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a < 0 }->{\\v > 0}")
    public static int toPositive(int a) {
        return -a;
    }

    @repair.regen.specification.Refinement("{a >= 0 }->{\\v <= 0}")
    public static int toNegative(int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        // @Refinement("\\v <= 0")
        // int c = a * (-10);
        if (a < 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = regen.test.project.SimpleTest.toPositive(a);
        } else {
            @repair.regen.specification.Refinement("c < 1")
            int c = regen.test.project.SimpleTest.toNegative(a);
            // @Refinement("\\v <= 0")
            // int c = a * -10;
        }
    }
}

