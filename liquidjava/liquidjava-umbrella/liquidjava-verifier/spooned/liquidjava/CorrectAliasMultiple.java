package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("type Positive(double x) { x > 0}")
@liquidjava.specification.RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class CorrectAliasMultiple {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("PtGrade(_)")
        double positiveGrade2 = (20 * 0.5) + (20 * 0.5);
        @liquidjava.specification.Refinement("Positive(_)")
        double positive = positiveGrade2;
    }
}

