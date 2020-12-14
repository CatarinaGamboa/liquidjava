package repair.regen;


public class ErrorIfAssignment {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        if (a > 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = a;
            b++;
            a = 10;
        }
    }
}

