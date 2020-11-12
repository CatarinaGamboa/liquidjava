package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 11;
        a = a * a;
        // if(a > 1) {
        // @Refinement("b > 0")
        // int b = a;
        // a = 0;
        // }
        // } else {
        // @Refinement("b <= 0")
        // int b = a;
        // }
    }
}

