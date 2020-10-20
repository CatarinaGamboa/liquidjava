package repair.regen;


public class CorrectAssignementAfterDeclaration {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("(z > 0) && (z < 50)")
        int z = 1;
        @repair.regen.specification.Refinement("u < 100")
        int u = 10;
        u = 11 + z;
        u = z * 2;
        u = 30 + z;
        @repair.regen.specification.Refinement("\\v > 0")
        int n = 1;
        n = (z + n) + (1 * n);
        @repair.regen.specification.Refinement("y > 0")
        int y = 15;
        y = y * y;
    }
}

