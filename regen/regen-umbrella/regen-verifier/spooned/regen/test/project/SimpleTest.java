package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        @repair.regen.specification.Refinement("\\v > a && \\v < 20")
        int b = 18;
        @repair.regen.specification.Refinement("\\v > b && \\v < 60")
        int c = 40;
        @repair.regen.specification.Refinement("true")
        int d = c;
        @repair.regen.specification.Refinement("\\v > c")
        int e = 80;
        @repair.regen.specification.Refinement("\\v > (c+c)")
        int f = 8000;
    }
}

