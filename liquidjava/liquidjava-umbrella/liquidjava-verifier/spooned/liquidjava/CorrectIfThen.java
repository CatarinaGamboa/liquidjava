package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectIfThen {
    public void have2(int a, int b) {
        @liquidjava.specification.Refinement("pos > 0")
        int pos = 10;
        if (a > 0) {
            if (a > b)
                pos = a - b;

        }
    }

    public void have1(int a) {
        @liquidjava.specification.Refinement("pos > 0")
        int pos = 10;
        if (a > 0) {
            pos = 5;
            pos = 8;
            pos = 30;
        }
        @liquidjava.specification.Refinement("_ == 30 || _ == 10")
        int u = pos;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        if (a > 0) {
            @liquidjava.specification.Refinement("b > 0")
            int b = a;
            b++;
            if (b > 10) {
                @liquidjava.specification.Refinement("_ > 0")
                int c = a;
                @liquidjava.specification.Refinement("_ > 11")
                int d = b + 1;
            }
            if (a > b) {
                @liquidjava.specification.Refinement("_ > b")
                int c = a;
            }
        }
    }
}

