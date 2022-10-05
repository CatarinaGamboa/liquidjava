package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectBooleanLitAndInvocations {
    @liquidjava.specification.Refinement("_ == (n > 10)")
    public static boolean greaterThanTen(int n) {
        return n > 10;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        @liquidjava.specification.Refinement("_ == true")
        boolean k = a < 11;
        @liquidjava.specification.Refinement("_ == true")
        boolean o = !(a == 12);
        @liquidjava.specification.Refinement("_ == false")
        boolean m = liquidjava.CorrectBooleanLitAndInvocations.greaterThanTen(a);
    }
}

