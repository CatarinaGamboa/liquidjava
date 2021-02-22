package repair.regen;


@repair.regen.specification.RefinementAlias("type Positive(int x) { x > 0}")
@repair.regen.specification.RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class ErrorAliasTypeMismatch {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("PtGrade(_)")
        double positiveGrade2 = (20 * 0.5) + (20 * 0.5);
        @repair.regen.specification.Refinement("Positive(_)")
        double positive = positiveGrade2;
    }
}

