package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectMethodInvocation {
    @liquidjava.specification.Refinement("_ == 2")
    private static int getTwo() {
        return 1 + 1;
    }

    @liquidjava.specification.Refinement("_ == 0")
    private static int getZero() {
        return 0;
    }

    @liquidjava.specification.Refinement("_ == 1")
    private static int getOne() {
        @liquidjava.specification.Refinement("_ == 0")
        int a = liquidjava.CorrectMethodInvocation.getZero();
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 1")
        int b = liquidjava.CorrectMethodInvocation.getZero();
        @liquidjava.specification.Refinement("_ > 0")
        int c = liquidjava.CorrectMethodInvocation.getOne();
        c = liquidjava.CorrectMethodInvocation.getTwo();
    }
}

