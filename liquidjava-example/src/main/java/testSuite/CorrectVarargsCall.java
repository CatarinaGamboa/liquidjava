package testSuite;

import liquidjava.specification.Refinement;

public class CorrectVarargsCall {

    static int count(String... names) {
        return names.length;
    }

    static int first(@Refinement("length(xs) > 0") int... xs) {
        return xs[0];
    }

    static int pick(@Refinement("k >= 0") int k, int... xs) {
        return k;
    }

    CorrectVarargsCall(@Refinement("length(xs) > 1") int... xs) {
    }

    public static void main(String[] args) {
        int a = count();
        int b = count("a");
        int c = count("a", "b", "c");
        int d = count(new String[] { "x" });
        int e = count(args);
        int f = first(1, 2);
        int g = first(5);
        int h = pick(0);
        int i = pick(1, 2, 3);
        int j = count("a", "b") + 1;
        new CorrectVarargsCall(1, 2);
    }
}
