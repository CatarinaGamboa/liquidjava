package liquidjava;


@java.lang.SuppressWarnings("unused")
@liquidjava.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class ErrorAliasArgumentSize {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("InRange( _, 10)")
        int j = 15;
    }
}

