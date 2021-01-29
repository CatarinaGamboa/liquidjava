package repair.regen;


public class CorrectAfterIfUsingScope {
    public static void main(java.lang.String[] args) {
        // Example 1
        @repair.regen.specification.Refinement("\\v < 100")
        int ielse = 90;
        @repair.regen.specification.Refinement("\\v < 10")
        int then = 7;
        if (then > 6)
            then = then - 8;
        else
            ielse = 5;

        @repair.regen.specification.Refinement("\\v == 7 || \\v == 5")
        int some = then;
        @repair.regen.specification.Refinement("\\v == 5 || \\v==90")
        int thing = ielse;
    }
}

