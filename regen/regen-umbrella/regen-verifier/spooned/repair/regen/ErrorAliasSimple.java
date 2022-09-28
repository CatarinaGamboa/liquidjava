package repair.regen;


@java.lang.SuppressWarnings("unused")
@repair.regen.specification.RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class ErrorAliasSimple {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("PtGrade(_)")
        double positiveGrade2 = (20 * 0.5) + (20 * 0.6);
    }
}

