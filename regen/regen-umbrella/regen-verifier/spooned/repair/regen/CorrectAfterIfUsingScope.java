package repair.regen;


public class CorrectAfterIfUsingScope {
    public static void main(java.lang.String[] args) {
        // Example 1
        @repair.regen.specification.Refinement("_ < 100")
        int ielse = 90;
        @repair.regen.specification.Refinement("_ < 10")
        int then = 7;
        if (then > 6)
            then = then - 8;
        else
            ielse = 5;

        @repair.regen.specification.Refinement("_ == 7 || _ == 5")
        int some = then;
        @repair.regen.specification.Refinement("_ == 5 || _==90")
        int thing = ielse;
    }
}

