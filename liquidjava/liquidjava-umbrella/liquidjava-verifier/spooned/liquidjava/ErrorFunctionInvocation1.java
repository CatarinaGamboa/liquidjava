package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorFunctionInvocation1 {
    @liquidjava.specification.Refinement("_ == 2")
    private static int getTwo() {
        return 1 + 1;
    }

    @liquidjava.specification.Refinement(" _ == 0")
    private static int getZero() {
        return 0;
    }

    @liquidjava.specification.Refinement("_ == 1")
    private static int getOne() {
        @liquidjava.specification.Refinement("_ == 0")
        int a = liquidjava.ErrorFunctionInvocation1.getZero();
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 1")
        int b = liquidjava.ErrorFunctionInvocation1.getZero();
        @liquidjava.specification.Refinement("_ > 0")
        int c = liquidjava.ErrorFunctionInvocation1.getOne();
        c = liquidjava.ErrorFunctionInvocation1.getZero();
    }
}

