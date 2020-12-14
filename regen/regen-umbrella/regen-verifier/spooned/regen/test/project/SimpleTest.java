package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // @Refinement("\\v < 10")
        // int a = 5;
        // 
        // if(a > 0) {
        // a = 100;
        // b++;
        // if(b > 10) {
        // @Refinement("\\v > 0")
        // int c = a;
        // @Refinement("\\v > 11")
        // int d = b+1;
        // }
        // if(a > b) {
        // @Refinement("\\v > b")
        // int c = a;
        // }
        // }
        // OTHER ERROR: if(!(a == 0))...
        // SHOULD BE ERROR -inconsitency a < 0 and a == 100 -> False prove->True
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 6;
        if (a < 0) {
            a = 5;
            @repair.regen.specification.Refinement("b < 0")
            int b = a;
        }
        a = 0;
    }
}

