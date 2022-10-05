package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectAssignementAfterDeclaration {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("(z > 0) && (z < 50)")
        int z = 1;
        @liquidjava.specification.Refinement("u < 100")
        int u = 10;
        u = 11 + z;
        u = z * 2;
        u = 30 + z;
        @liquidjava.specification.Refinement("_ > 0")
        int n = 1;
        n = (z + n) + (1 * n);
        @liquidjava.specification.Refinement("y > 0")
        int y = 15;
        y = y * y;
    }
}

