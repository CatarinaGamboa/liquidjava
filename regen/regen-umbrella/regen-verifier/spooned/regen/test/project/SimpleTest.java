package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{k >= 0} -> {\\v == 0}")
    public static int untilZero(int k) {
        if (k == 0)
            return 0;
        else
            return regen.test.project.SimpleTest.untilZero((k - 1));

    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("b < 3")
        int b = regen.test.project.SimpleTest.untilZero(5);
    }
}

