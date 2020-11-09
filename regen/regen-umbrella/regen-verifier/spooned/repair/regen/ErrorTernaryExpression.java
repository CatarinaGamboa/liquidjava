package repair.regen;


public class ErrorTernaryExpression {
    @repair.regen.specification.Refinement("{\\v == 3}")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        a = (a == 2) ? 6 + (repair.regen.ErrorTernaryExpression.three()) : 4 * (repair.regen.ErrorTernaryExpression.three());
    }
}

