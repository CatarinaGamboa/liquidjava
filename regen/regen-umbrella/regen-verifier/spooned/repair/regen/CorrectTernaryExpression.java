package repair.regen;


public class CorrectTernaryExpression {
    @repair.regen.specification.Refinement("{\\v == 3}")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        @repair.regen.specification.Refinement("\\v > 0")
        int b = 3;
        a = (a == 2) ? 6 : 9;
        a = (b > 2) ? 8 : -1;
        b = (a < 100) ? repair.regen.CorrectTernaryExpression.three() : (repair.regen.CorrectTernaryExpression.three()) - 1;
        @repair.regen.specification.Refinement("c < 100")
        int c = (a < 100) ? repair.regen.CorrectTernaryExpression.three() : a;
        c = (a < 100) ? (repair.regen.CorrectTernaryExpression.three()) * 3 : a * 5;
    }
}

