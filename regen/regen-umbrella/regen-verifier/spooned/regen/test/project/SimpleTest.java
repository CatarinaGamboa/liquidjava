package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("\\v == 3")
    public static int three() {
        return 3;
    }

    @repair.regen.specification.Refinement("{true}->{ \\v == (n > 10) }")
    public static boolean greaterThanTen(int n) {
        return n > 10;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        @repair.regen.specification.Refinement("\\v == true")
        boolean k = a < 11;
        @repair.regen.specification.Refinement("\\v == true")
        boolean t = !(a == 12);
        @repair.regen.specification.Refinement("\\v == true")
        boolean m = regen.test.project.SimpleTest.greaterThanTen(a);
        // if(a < 0) {
        // @Refinement("b < 0")
        // int b = a;
        // } else {
        // @Refinement("b >= 0")
        // int b = a;
        // }
        // @Refinement("\\v > 10")
        // int a = 11;
        // if(a > three()) {
        // a = 15;
        // }else {
        // a = -10;
        // }
        // } else {
        // @Refinement("b <= 0")
        // int b = a;
        // }
    }
}

