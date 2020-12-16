package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        a = 9;
        @repair.regen.specification.Refinement("\\v > a")
        int b = 40;
        a = b;
    }
}

