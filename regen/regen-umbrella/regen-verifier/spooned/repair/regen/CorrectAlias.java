package repair.regen;


@repair.regen.specification.RefinementAlias("type PtGrade(int x) { x >= 0 && x <= 20}")
public class CorrectAlias {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("PtGrade(_) && _ >= 10")
        int positiveGrade = 15;
        @repair.regen.specification.Refinement("_ > 5 && _ < 18")
        int nGrade = 10;
        @repair.regen.specification.Refinement("PtGrade(_) && PtGrade(nGrade)")
        int positiveGrade2 = 15;
    }
}

