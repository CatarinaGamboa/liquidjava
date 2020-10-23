package repair.regen;


public class ErrorNoRefinementsInVar {
    public static void main(java.lang.String[] args) {
        int a = 3;
        @repair.regen.specification.Refinement("b < 10")
        int b = a;
    }
}

