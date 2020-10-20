package repair.regen;


public class CorrectSimpleAssignment {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 0")
        int a = 1;
        @repair.regen.specification.Refinement("b == 2 || b == 3 || b == 4")
        int b = 2;
        @repair.regen.specification.Refinement("d >= 2")
        int d = b;// should be okay

    }
}

