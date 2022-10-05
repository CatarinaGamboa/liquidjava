package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("type Positive(int x) { x > 0}")
@liquidjava.specification.RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class ErrorAliasTypeMismatch {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("PtGrade(_)")
        double positiveGrade2 = (20 * 0.5) + (20 * 0.5);
        @liquidjava.specification.Refinement("Positive(_)")
        double positive = positiveGrade2;
        // Positive(_)   fica   positive > 0
    }
}

