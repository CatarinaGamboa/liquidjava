package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 0")
        int a = -6;
        @repair.regen.specification.Refinement("b > 0")
        int b = 8;
        a = -3;
        a = -(6 + 5);
        b = -(-10);
        b = -b;
        // b = -b;
    }
}

