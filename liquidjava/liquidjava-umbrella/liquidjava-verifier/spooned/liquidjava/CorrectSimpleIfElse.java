package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectSimpleIfElse {
    @liquidjava.specification.Refinement("_ > 0")
    public static int toPositive(@liquidjava.specification.Refinement("a < 0")
    int a) {
        return -a;
    }

    @liquidjava.specification.Refinement("_ < 0")
    public static int toNegative(@liquidjava.specification.Refinement("a > 0")
    int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        if (a < 0) {
            @liquidjava.specification.Refinement("b < 0")
            int b = a;
        } else {
            @liquidjava.specification.Refinement("b >= 0")
            int b = a;
        }
        // EXAMPLE 2
        @liquidjava.specification.Refinement("_ < 10")
        int ex_a = 5;
        if (ex_a < 0) {
            @liquidjava.specification.Refinement("_ >= 10")
            int ex_b = (liquidjava.CorrectSimpleIfElse.toPositive(ex_a)) * 10;
        } else {
            if (ex_a != 0) {
                @liquidjava.specification.Refinement("_ < 0")
                int ex_d = liquidjava.CorrectSimpleIfElse.toNegative(ex_a);
            }
            @liquidjava.specification.Refinement("_ < ex_a")
            int ex_c = -10;
        }
    }
}

