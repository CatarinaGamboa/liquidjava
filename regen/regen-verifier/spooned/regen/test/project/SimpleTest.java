package regen.test.project;


public class SimpleTest {
    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        int a = 1;
        @repair.regen.specification.Refinement("b > 3")
        int b = 2;// should emit error

        @repair.regen.specification.Refinement("c > 1")
        int c = 2;// should be OK

    }
}

