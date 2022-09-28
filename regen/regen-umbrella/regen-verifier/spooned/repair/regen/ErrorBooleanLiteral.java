package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorBooleanLiteral {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        @repair.regen.specification.Refinement("_ == true")
        boolean k = a < 11;
        @repair.regen.specification.Refinement("_ == false")
        boolean t = !(a == 12);
    }
}

