package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectUnaryOperators {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int v = 3;
        v--;
        @liquidjava.specification.Refinement("_ >= 10")
        int s = 100;
        s++;
        @liquidjava.specification.Refinement("_ < 0")
        int a = -6;
        @liquidjava.specification.Refinement("b > 0")
        int b = 8;
        a = -3;
        a = -(6 + 5);
        b = -a;
        b = -(-10);
        b = +3;
        b = +s;
        @liquidjava.specification.Refinement("_ <= 0")
        int c = 5 * (-10);
    }
}

