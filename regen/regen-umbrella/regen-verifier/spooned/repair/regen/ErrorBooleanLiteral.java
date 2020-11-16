package repair.regen;


public class ErrorBooleanLiteral {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        @repair.regen.specification.Refinement("\\v == true")
        boolean k = a < 11;
        @repair.regen.specification.Refinement("\\v == false")
        boolean t = !(a == 12);
    }
}

