package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorUnaryOperators {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int v = 3;
        v--;
        @repair.regen.specification.Refinement("_ >= 10")
        int s = 10;
        s--;
    }
}

