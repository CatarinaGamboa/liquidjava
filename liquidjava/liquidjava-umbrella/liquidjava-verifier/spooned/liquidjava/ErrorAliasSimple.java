package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class ErrorAliasSimple {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("PtGrade(_)")
        double positiveGrade2 = (20 * 0.5) + (20 * 0.6);
    }
}

