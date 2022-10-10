package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectNoRefinements {
    private static int addOne(int i) {
        return i + 1;
    }

    private static int one() {
        return 1;
    }

    public static void main(java.lang.String[] args) {
        int a = liquidjava.CorrectNoRefinements.one();
        int b = a;
        int c = ((a * b) + 50) + (liquidjava.CorrectNoRefinements.addOne(5));
        b = 5;
        int s = a + 10;
        s++;
    }
}

