package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSyntax1 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 100 +")
        int value = 90 + 4;
    }
}

