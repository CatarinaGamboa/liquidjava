package liquidjava;


public class ErrorTernaryExpression {
    @liquidjava.specification.Refinement(" _ == 3")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        a = (a == 2) ? 6 + (liquidjava.ErrorTernaryExpression.three()) : 4 * (liquidjava.ErrorTernaryExpression.three());
    }
}

