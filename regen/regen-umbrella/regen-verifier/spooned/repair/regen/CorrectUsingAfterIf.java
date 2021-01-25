package repair.regen;


public class CorrectUsingAfterIf {
    public static void main(java.lang.String[] args) {
        // Example 1
        @repair.regen.specification.Refinement("\\v > 5")
        int a = 6;
        if (a > 8)
            a = 20;
        else
            a = 30;

        @repair.regen.specification.Refinement("\\v == 30 || \\v == 20")
        int b = a;
        // Example 2
        @repair.regen.specification.Refinement("y < 100")
        int y = 50;
        if (y > 2)
            y = 3;
        else
            y = 6;

        @repair.regen.specification.Refinement("z < 7")
        int z = y;
    }
}

