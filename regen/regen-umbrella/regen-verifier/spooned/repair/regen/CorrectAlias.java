package repair.regen;


@repair.regen.specification.RefinementAlias("type PtGrade(int x) { x >= 0 && x <= 20}")
public class CorrectAlias {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("PtGrade(_) && _ >= 10")
        int positiveGrade = 15;
    }
}

