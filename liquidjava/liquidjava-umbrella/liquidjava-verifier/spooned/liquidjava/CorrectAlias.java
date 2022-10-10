package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("type PtGrade(int x) { x >= 0 && x <= 20}")
public class CorrectAlias {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("PtGrade(_) && _ >= 10")
        int positiveGrade = 15;
        @liquidjava.specification.Refinement("_ > 5 && _ < 18")
        int nGrade = 10;
        @liquidjava.specification.Refinement("PtGrade(_) && PtGrade(nGrade)")
        int positiveGrade2 = 15;
    }
}

