package regen.test.project;


public class SimpleTest {
    // 
    // @Refinement("{a > 10}->{ \\v > 0}")
    // public static int doubleBiggerThanTen(int a){
    // return a*2;
    // }
    // 
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 6;
        if (a > 3) {
            a = 7 + 1;
        }// else {

        // @Refinement("b > 8")
        // int b = a;
        // 
        // }
        // @Refinement("b > 8")
        // int b = a;
    }
}

