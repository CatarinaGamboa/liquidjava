package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorFunctionInvocation {
    @liquidjava.specification.Refinement(" _ >= a")
    public static int posMult(@liquidjava.specification.Refinement("a == 10")
    int a, @liquidjava.specification.Refinement("_ < a && _ > 0")
    int b) {
        @liquidjava.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 10")
        int p = 10;
        p = liquidjava.ErrorFunctionInvocation.posMult(10, 4);
    }
}

