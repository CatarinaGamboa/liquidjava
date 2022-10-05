package liquidjava;


public class CorrectSimpleIfThen {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("\\v < 10")
        int a = 5;
        if (a > 0) {
            @liquidjava.specification.Refinement("b > 0")
            int b = a;
            b++;
            if (b > 10) {
                @liquidjava.specification.Refinement("\\v > 0")
                int c = a;
                @liquidjava.specification.Refinement("\\v > 11")
                int d = b + 1;
            }
        }
    }
}

