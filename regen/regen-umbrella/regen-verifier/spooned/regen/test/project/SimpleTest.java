package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{\\v == 3}")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        a = (a == 2) ? 6 + (regen.test.project.SimpleTest.three()) : 4 * (regen.test.project.SimpleTest.three());
    }
}

