package repair.regen;


public class CorrectReadSpecificAssignment {
    public void testAssignements() {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        @repair.regen.specification.Refinement("\\v > 14")
        int b = a;
        a = 12;
        @repair.regen.specification.Refinement("\\v >= 15")
        int c = b;
        b = 16;
        @repair.regen.specification.Refinement("\\v > 14")
        int d = c;
    }

    public void testIfs() {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        if (a > 14) {
            @repair.regen.specification.Refinement("\\v > 14")
            int b = a;
            a = 12;
            @repair.regen.specification.Refinement("\\v < 14")
            int c = a;
        }
    }

    public static void main(java.lang.String[] args) {
    }
}

