package repair.regen;


public class CorrectSimpleIfElse {
    @repair.regen.specification.Refinement("{a < 0 }->{\\v > 0}")
    public static int toPositive(int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        if (a < 0) {
            @repair.regen.specification.Refinement("b < 0")
            int b = a;
        } else {
            @repair.regen.specification.Refinement("b >= 0")
            int b = a;
        }
        // EXAMPLE 2
        @repair.regen.specification.Refinement("\\v < 10")
        int ex_a = 5;
        if (ex_a < 0) {
            @repair.regen.specification.Refinement("\\v >= 10")
            int ex_b = (repair.regen.CorrectSimpleIfElse.toPositive(ex_a)) * 10;
        } else {
            @repair.regen.specification.Refinement("\\v < a")
            int ex_c = -10;
        }
    }
}

