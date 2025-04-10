package testSuite;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectUnaryOperators {
    public static void main(String[] args) {
        @Refinement("_ < 10")
        int v = 3;
        v--;
        @Refinement("_ >= 10")
        int s = 100;
        s++;

        @Refinement("_ < 0")
        int a = -6;
        @Refinement("b > 0")
        int b = 8;

        a = -3;
        a = -(6 + 5);
        b = -a;
        b = -(-10);
        b = +3;
        b = +s;

        @Refinement("_ <= 0")
        int c = 5 * (-10);
    }
}
