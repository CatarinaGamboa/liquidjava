package regen.test.project;


public class SimpleTest {
    // public void testAssignements() {
    // @Refinement("\\v > 10")
    // int a = 15;
    // 
    // @Refinement("\\v > 14")
    // int b = a;
    // 
    // a = 12;
    // 
    // @Refinement("\\v >= 15")
    // int c = b;
    // b = 16;
    // 
    // @Refinement("\\v > 14")
    // int d = c;
    // }
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        if (a > 14) {
            a = 12;
            @repair.regen.specification.Refinement("\\v < 11")
            int c = a;
        }
    }
}

