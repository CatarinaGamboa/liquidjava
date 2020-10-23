package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("b != 5")
        int b = 5;
    }
}

