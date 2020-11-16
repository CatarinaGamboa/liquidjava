package repair.regen;


public class CorrectBooleanLitAndInvocations {
    @repair.regen.specification.Refinement("{true}->{ \\v == (n > 10) }")
    public static boolean greaterThanTen(int n) {
        return n > 10;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        @repair.regen.specification.Refinement("\\v == true")
        boolean k = a < 11;
        @repair.regen.specification.Refinement("\\v == true")
        boolean t = !(a == 12);
        @repair.regen.specification.Refinement("\\v == false")
        boolean m = repair.regen.CorrectBooleanLitAndInvocations.greaterThanTen(a);
    }
}
