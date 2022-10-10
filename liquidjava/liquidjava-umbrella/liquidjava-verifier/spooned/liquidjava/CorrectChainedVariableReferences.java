package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectChainedVariableReferences {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        @liquidjava.specification.Refinement("_ > a && _ < 20")
        int b = 18;
        @liquidjava.specification.Refinement("_ > b && _ < 60")
        int c = 40;
        @liquidjava.specification.Refinement("true")
        int d = c;
        @liquidjava.specification.Refinement("_ > c")
        int e = 80;
        @liquidjava.specification.Refinement("_ > (c+c)")
        int f = 8000;
    }
}

