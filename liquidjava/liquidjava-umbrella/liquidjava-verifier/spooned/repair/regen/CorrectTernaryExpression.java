package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectTernaryExpression {
    @liquidjava.specification.Refinement("_ == 3")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        @liquidjava.specification.Refinement("_ > 0")
        int b = 3;
        a = (a == 2) ? 6 : 9;
        a = (b > 2) ? 8 : -1;
        b = (a < 100) ? liquidjava.CorrectTernaryExpression.three() : (liquidjava.CorrectTernaryExpression.three()) - 1;
        @liquidjava.specification.Refinement("c < 100")
        int c = (a < 100) ? liquidjava.CorrectTernaryExpression.three() : a;
        c = (a < 100) ? (liquidjava.CorrectTernaryExpression.three()) * 3 : a * 5;
    }
}

