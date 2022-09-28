package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorAssignementAfterDeclaration {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("(z > 0) && (z < 50)")
        int z = 1;
        @repair.regen.specification.Refinement("u < 100")
        int u = 10;
        u = 11 + z;
        u = z * 2;
        u = 30 + z;
        u = 500;// error

    }
}

