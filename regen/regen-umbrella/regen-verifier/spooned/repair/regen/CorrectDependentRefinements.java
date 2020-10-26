package repair.regen;


public class CorrectDependentRefinements {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int smaller = 5;
        @repair.regen.specification.Refinement("bigger > 20")
        int bigger = 50;
        @repair.regen.specification.Refinement("\\v > smaller  && \\v < bigger")
        int middle = 15;
        @repair.regen.specification.Refinement("\\v >= smaller")
        int k = 10;
        @repair.regen.specification.Refinement("\\v <= bigger")
        int y = 10;
        @repair.regen.specification.Refinement("\\v == 20")
        int x1 = 20;
        @repair.regen.specification.Refinement("\\v == x1 + 1")
        int x2 = 21;
        @repair.regen.specification.Refinement("\\v == x1 - 1")
        int x3 = 19;
        @repair.regen.specification.Refinement("\\v == x1 * 5")
        int x4 = x1 * 5;
        @repair.regen.specification.Refinement("\\v == x1 / 2")
        int x5 = 10;
        @repair.regen.specification.Refinement("\\v == x1 % 2")
        int x6 = 0;
        @repair.regen.specification.Refinement("(-x7) < x1")
        int x7 = 0;
        @repair.regen.specification.Refinement("\\v != x1")
        int x8 = 0;
        @repair.regen.specification.Refinement("\\v == 30")
        int o = 30;
        @repair.regen.specification.Refinement("\\v == x1 || \\v == o ")
        int x9 = 20;
    }
}

