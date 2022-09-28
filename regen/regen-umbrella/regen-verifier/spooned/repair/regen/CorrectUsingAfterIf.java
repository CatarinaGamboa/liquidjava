package repair.regen;


@java.lang.SuppressWarnings("unused")
public class CorrectUsingAfterIf {
    public void have2(int a, int b) {
        @repair.regen.specification.Refinement("pos > 0")
        int pos = 10;
        if ((a > 0) && (b > 0)) {
            pos = a;
        } else
            if (b > 0)
                pos = b;


        @repair.regen.specification.Refinement("_ == a || _ == b || _ == 10")
        int r = pos;
    }

    public static void main(java.lang.String[] args) {
        // Example 1
        @repair.regen.specification.Refinement("_ > 5")
        int a = 6;
        if (a > 8)
            a = 20;
        else
            a = 30;

        @repair.regen.specification.Refinement("_ == 30")
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
        @repair.regen.specification.Refinement("_ < 100")
        int changedInThenAndElse = 10;
        @repair.regen.specification.Refinement("_ > 6")
        int changeOnlyInThen = 7;
        if (changedInThenAndElse > 2) {
            changedInThenAndElse = 3;
            changeOnlyInThen = 8;
        } else {
            changedInThenAndElse = 6;
        }
        @repair.regen.specification.Refinement("_ < 7")
        int ze1 = changedInThenAndElse;
        @repair.regen.specification.Refinement("_ < 9")
        int ze2 = changeOnlyInThen;
        // Example 4
        @repair.regen.specification.Refinement("_ < 100")
        int initializedInThen;
        if (true)
            initializedInThen = 7;

        @repair.regen.specification.Refinement("_ == 35")
        int hello = initializedInThen * 5;
        // Example 5
        @repair.regen.specification.Refinement("_ < 100")
        int initializedInElse;
        int asds;
        if (false)
            asds = 5;
        else
            initializedInElse = 8;

        @repair.regen.specification.Refinement("_ == 40")
        int world = initializedInElse * 5;
        // Example 7
        @repair.regen.specification.Refinement("k > 0 && k < 100")
        int k = 5;
        if (k > 7) {
            k = 9;
        }
        @repair.regen.specification.Refinement("_ < 10")
        int m = k;
        k = 50;
        @repair.regen.specification.Refinement("_ == 50")
        int m2 = k;
    }
}

