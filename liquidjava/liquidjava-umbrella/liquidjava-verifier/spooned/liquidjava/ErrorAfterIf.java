package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorAfterIf {
    public void have2(int a, int b) {
        @liquidjava.specification.Refinement("pos > 0")
        int pos = 10;
        if ((a > 0) && (b > 0)) {
            pos = a;
        } else {
            if (b > 0)
                pos = b;

        }
        @liquidjava.specification.Refinement("_ == a || _ == b")
        int r = pos;
    }
}

