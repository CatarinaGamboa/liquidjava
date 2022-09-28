package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorFunctionInvocation1 {
    @repair.regen.specification.Refinement("_ == 2")
    private static int getTwo() {
        return 1 + 1;
    }

    @repair.regen.specification.Refinement(" _ == 0")
    private static int getZero() {
        return 0;
    }

    @repair.regen.specification.Refinement("_ == 1")
    private static int getOne() {
        @repair.regen.specification.Refinement("_ == 0")
        int a = repair.regen.ErrorFunctionInvocation1.getZero();
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 1")
        int b = repair.regen.ErrorFunctionInvocation1.getZero();
        @repair.regen.specification.Refinement("_ > 0")
        int c = repair.regen.ErrorFunctionInvocation1.getOne();
        c = repair.regen.ErrorFunctionInvocation1.getZero();
    }
}

