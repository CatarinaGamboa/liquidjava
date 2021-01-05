package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a > 10}->{ \\v > 0}")
    public static int doubleBiggerThanTen(int a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        // EXAMPLE VARIABLE INSIDE REFINEMENT
        // @Refinement("\\v < 10")
        // int a = 6;
        // if(a > 0) {
        // a = -2;
        // @Refinement("b < a")
        // int b = -3;
        // 
        // }
        @repair.regen.specification.Refinement("a > 0")
        int a = 50;
        int b = regen.test.project.SimpleTest.doubleBiggerThanTen(a);
    }
}

