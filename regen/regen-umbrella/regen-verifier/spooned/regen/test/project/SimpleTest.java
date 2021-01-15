package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // @Refinement("\\v == 6")
        // int a = Math.abs(6);
        @repair.regen.specification.Refinement("\\v < 1")
        int b = regen.test.project.SimpleTest.getZero();
        @repair.regen.specification.Refinement("\\v > 0")
        int c = regen.test.project.SimpleTest.getOne();
        // @Refinement("\\v > 0")
        // double e = Math.sqrt(6);
        // @Refinement("\\v >= 0")
        // double c = Math.random();
        // @Refinement("b > 0")
        // int b = Math.addExact(6, 2);
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }

    @repair.regen.specification.Refinement("{\\v == 0}")
    private static int getZero() {
        return 0;
    }

    @repair.regen.specification.Refinement("{\\v == 1}")
    private static int getOne() {
        @repair.regen.specification.Refinement("\\v == 0")
        int a = regen.test.project.SimpleTest.getZero();
        return a + 1;
    }
}

