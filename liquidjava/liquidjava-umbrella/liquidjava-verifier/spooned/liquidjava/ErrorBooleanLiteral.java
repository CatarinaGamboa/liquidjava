package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorBooleanLiteral {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        @liquidjava.specification.Refinement("_ == true")
        boolean k = a < 11;
        @liquidjava.specification.Refinement("_ == false")
        boolean t = !(a == 12);
    }
}

