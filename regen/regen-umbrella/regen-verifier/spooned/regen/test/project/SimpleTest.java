package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        // @Refinement("\\v < 10")
        // int a = 5;
        // 
        // if(a > 0) {
        // a = 12;
        // }
        int a = 3;
        @repair.regen.specification.Refinement("b < 10")
        int b = a;
    }
}

