package repair.regen;


@java.lang.SuppressWarnings("unused")
public class CorrectSimpleIfElse {
    @repair.regen.specification.Refinement("_ > 0")
    public static int toPositive(@repair.regen.specification.Refinement("a < 0")
    int a) {
        return -a;
    }

    @repair.regen.specification.Refinement("_ < 0")
    public static int toNegative(@repair.regen.specification.Refinement("a > 0")
    int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        if (a < 0) {
            @repair.regen.specification.Refinement("b < 0")
            int b = a;
        } else {
            @repair.regen.specification.Refinement("b >= 0")
            int b = a;
        }
        // EXAMPLE 2
        @repair.regen.specification.Refinement("_ < 10")
        int ex_a = 5;
        if (ex_a < 0) {
            @repair.regen.specification.Refinement("_ >= 10")
            int ex_b = (repair.regen.CorrectSimpleIfElse.toPositive(ex_a)) * 10;
        } else {
            if (ex_a != 0) {
                @repair.regen.specification.Refinement("_ < 0")
                int ex_d = repair.regen.CorrectSimpleIfElse.toNegative(ex_a);
            }
            @repair.regen.specification.Refinement("_ < ex_a")
            int ex_c = -10;
        }
    }
}

