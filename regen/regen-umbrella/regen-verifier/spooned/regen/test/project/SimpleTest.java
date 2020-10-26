package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a == 10")
        int a = 10;
        @repair.regen.specification.Refinement("b > a")
        int b = 50;
        // 
        // int a = 3;
        // @Refinement("b < 10")
        // int b = a;
    }
}

