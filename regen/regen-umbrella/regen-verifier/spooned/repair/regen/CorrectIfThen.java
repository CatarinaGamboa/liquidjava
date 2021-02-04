package repair.regen;


public class CorrectIfThen {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        if (a > 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = a;
            b++;
            if (b > 10) {
                @repair.regen.specification.Refinement("_ > 0")
                int c = a;
                @repair.regen.specification.Refinement("_ > 11")
                int d = b + 1;
            }
            if (a > b) {
                @repair.regen.specification.Refinement("_ > b")
                int c = a;
            }
        }
    }
}

