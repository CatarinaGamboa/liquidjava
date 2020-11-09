package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        int a = 3;
        @repair.regen.specification.Refinement("b < 10")
        int b = a;
        // if(a > 0) {
        // a = 12;
        // }//a > 0 && a == 12 <: a < 10
    }
}

