package liquidjava;


public class ErrorFunctionDeclarations {
    @liquidjava.specification.Refinement("_ >= d && _ < i")
    private static int range(@liquidjava.specification.Refinement("d >= 0")
    int d, @liquidjava.specification.Refinement("i > d")
    int i) {
        return i + 1;
    }
}

