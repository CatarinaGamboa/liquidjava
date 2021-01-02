package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        @repair.regen.specification.Refinement("\\v > 12")
        int k = a;
    }
}

