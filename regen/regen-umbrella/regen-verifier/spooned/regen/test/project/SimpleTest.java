package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int smaller = 5;
        @repair.regen.specification.Refinement("bigger > 20")
        int bigger = 50;
        @repair.regen.specification.Refinement("\\v > smaller  && \\v < bigger")
        int middle = 15;
    }
}

