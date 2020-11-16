package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        if (a < 0) {
            @repair.regen.specification.Refinement("b < 0")
            int b = a;
        } else {
            @repair.regen.specification.Refinement("b >= 0")
            int b = a;
        }
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

