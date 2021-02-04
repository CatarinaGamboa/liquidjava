package repair.regen;


public class CorrectBooleanLitAndInvocations {
    @repair.regen.specification.Refinement("{true}->{ _ == (n > 10) }")
    public static boolean greaterThanTen(int n) {
        return n > 10;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        @repair.regen.specification.Refinement("_ == true")
        boolean k = a < 11;
        @repair.regen.specification.Refinement("_ == true")
        boolean o = !(a == 12);
        @repair.regen.specification.Refinement("_ == false")
        boolean m = repair.regen.CorrectBooleanLitAndInvocations.greaterThanTen(a);
    }
}

