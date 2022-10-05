package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectAfterIfUsingScope {
    public static void main(java.lang.String[] args) {
        // Example 1
        @liquidjava.specification.Refinement("_ < 100")
        int ielse = 90;
        @liquidjava.specification.Refinement("_ < 10")
        int then = 7;
        if (then > 6)
            then = then - 2;
        else
            ielse = 5;

        @liquidjava.specification.Refinement("_ == 7 || _ == 5")
        int some = then;
        @liquidjava.specification.Refinement("_ == 5 || _==90")
        int thing = ielse;
        // EXAMPLE 2
        @liquidjava.specification.Refinement("_ < 100")
        int value = 90;
        if (value > 6) {
            @liquidjava.specification.Refinement("_ > 10")
            int innerScope = 30;
            value = innerScope;
        }
        @liquidjava.specification.Refinement("_ == 30 || _ == 90")
        int some2 = value;
    }
}

