

@repair.regen.specification.RefinementAlias("Nat(int x) {x >= 0}")
@repair.regen.specification.RefinementAlias("GreaterEqualThan(int x, int y) {x >= y}")
public class SumRecursionLJ {
    // recursion + ifs -> Example tutorial refinement types
    @repair.regen.specification.Refinement("Nat(_) && _ >= n")
    public static int sum(int n) {
        // 1
        if (n <= 0)
            return 0;
        else {
            int t1 = SumRecursionLJ.sum((n - 1));
            return n + t1;
        }
    }

    // recursion + ifs -> Common example - used in presentation (print)
    @repair.regen.specification.Refinement("_ >= 1 && GreaterEqualThan(_, n)")
    public static int fibonacci(@repair.regen.specification.Refinement("Nat(n)")
    int n) {
        if (n <= 1)
            return 1;
        else// error: change to n

            return n * (SumRecursionLJ.fibonacci((n - 1)));

    }

    // ifs -> Example tutorial refinement types
    @repair.regen.specification.Refinement("(n < 0) ? (_ == -n) : (_ == n)")
    public static int absolute(int n) {
        if (0 <= n)
            return -(-n);
        else// error: leave -n

            return 0 - n;

    }

    // ifs -> very simple
    @repair.regen.specification.Refinement("(a < b)? (_ == b) : (_ == a)")
    public static int max(int a, int b) {
        // change signal
        if (a < b)
            return b;
        else
            return a;

    }

    public static double getGrade(int exam, int essay, int participation, int bonus) {
        @repair.regen.specification.Refinement("_ == 0.5")
        double valueExam = 0.5;
        @repair.regen.specification.Refinement("_ == 0.25")
        double valueEssay = 0.25;
        @repair.regen.specification.Refinement("_ == 0.12")
        double valueParticipation = 0.12;
        @repair.regen.specification.Refinement("(valueExam + valueEssay + valueParticipation + _ ) == 1")
        double valueBonus = 0.13;// 0.17 error

        return (((valueExam * exam) + (valueEssay * essay)) + (valueParticipation * participation)) + (valueBonus * bonus);
    }
}

