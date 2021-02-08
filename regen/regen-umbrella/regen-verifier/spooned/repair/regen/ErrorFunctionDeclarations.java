package repair.regen;


public class ErrorFunctionDeclarations {
    @repair.regen.specification.Refinement("_ >= d && _ < i")
    private static int range(@repair.regen.specification.Refinement("d >= 0")
    int d, @repair.regen.specification.Refinement("i > d")
    int i) {
        return i + 1;
    }
}

