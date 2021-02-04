package repair.regen;


public class ErrorFunctionDeclarations {
    @repair.regen.specification.Refinement("{d >= 0}->{i > d}->{_ >= d && _ < i}")
    private static int range(int d, int i) {
        return i + 1;
    }
}

