package repair.regen;


public class ErrorSpecificValuesIf2 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 10")
        int a = 15;
        if (a > 14) {
            a = 12;
            @repair.regen.specification.Refinement("_ < 11")
            int c = a;
        }
    }
}

