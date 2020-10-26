package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a > 20}->{ \\v == a + 1}")
    private static int addOne(int a) {
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 0")
        int a = 6;
        @repair.regen.specification.Refinement("\\v > 0")
        int b = (regen.test.project.SimpleTest.addOne(50)) + a;
    }
}

