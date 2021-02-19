package repair.regen;


public class CorrectIfThen {
    public void have2(int a, int b) {
        @repair.regen.specification.Refinement("pos > 0")
        int pos = 10;
        if (a > 0) {
            if (a > b)
                pos = a - b;

        }
    }

    public void have1(int a) {
        @repair.regen.specification.Refinement("pos > 0")
        int pos = 10;
        if (a > 0) {
            pos = 5;
            pos = 8;
            pos = 30;
        }
        @repair.regen.specification.Refinement("_ == 30 || _ == 10")
        int u = pos;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        if (a > 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = a;
            b++;
            if (b > 10) {
                @repair.regen.specification.Refinement("_ > 0")
                int c = a;
                @repair.regen.specification.Refinement("_ > 11")
                int d = b + 1;
            }
            if (a > b) {
                @repair.regen.specification.Refinement("_ > b")
                int c = a;
            }
        }
    }
}

