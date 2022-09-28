package repair.regen;


@java.lang.SuppressWarnings("unused")
@repair.regen.specification.RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class ErrorAliasArgumentSize {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("InRange( _, 10)")
        int j = 15;
    }
}

