package repair.regen;


@java.lang.SuppressWarnings("unused")
public class CorrectChainedVariableReferences {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        @repair.regen.specification.Refinement("_ > a && _ < 20")
        int b = 18;
        @repair.regen.specification.Refinement("_ > b && _ < 60")
        int c = 40;
        @repair.regen.specification.Refinement("true")
        int d = c;
        @repair.regen.specification.Refinement("_ > c")
        int e = 80;
        @repair.regen.specification.Refinement("_ > (c+c)")
        int f = 8000;
    }
}

