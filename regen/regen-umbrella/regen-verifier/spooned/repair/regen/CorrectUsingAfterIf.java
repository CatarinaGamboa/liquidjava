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
        // Example 3
        @repair.regen.specification.Refinement("\\v < 100")
        int changedInThenAndElse = 10;
        @repair.regen.specification.Refinement("\\v > 6")
        int changeOnlyInThen = 7;
        if (changedInThenAndElse > 2) {
            changedInThenAndElse = 3;
            changeOnlyInThen = 8;
        } else {
            changedInThenAndElse = 6;
        }
        @repair.regen.specification.Refinement("\\v < 7")
        int ze1 = changedInThenAndElse;
        @repair.regen.specification.Refinement("\\v < 9")
        int ze2 = changeOnlyInThen;
        // Example 4
        @repair.regen.specification.Refinement("\\v < 100")
        int initializedInThen;
        if (true)
            initializedInThen = 7;

        @repair.regen.specification.Refinement("\\v == 35")
        int hello = initializedInThen * 5;
        // Example 5
        @repair.regen.specification.Refinement("\\v < 100")
        int initializedInElse;
        int asds;
        if (false)
            asds = 5;
        else
            initializedInElse = 8;

        @repair.regen.specification.Refinement("\\v == 40")
        int world = initializedInElse * 5;
        // Example 7
        @repair.regen.specification.Refinement("k > 0 && k < 100")
        int k = 5;
        if (k > 7) {
            k = 9;
        }
        @repair.regen.specification.Refinement("\\v < 10")
        int m = k;
        k = 50;
        @repair.regen.specification.Refinement("\\v == 50")
        int m2 = k;
    }
}
