package repair.regen;


public class CorrectSimpleIfThen {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        if (a > 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = a;
            b++;
            if (b > 10) {
                @repair.regen.specification.Refinement("\\v > 0")
                int c = a;
                @repair.regen.specification.Refinement("\\v > 11")
                int d = b + 1;
            }
        }
    }
}

