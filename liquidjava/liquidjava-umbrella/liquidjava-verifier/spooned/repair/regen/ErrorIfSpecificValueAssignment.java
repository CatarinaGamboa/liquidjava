package liquidjava;


public class ErrorIfSpecificValueAssignment {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("\\v > 10")
        int a = 15;
        if (a > 14) {
            a = 12;
            @liquidjava.specification.Refinement("\\v < 11")
            int c = a;
        }
    }
}

