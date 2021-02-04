package repair.regen;


public class CorrectUnaryOperators {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int v = 3;
        v--;
        @repair.regen.specification.Refinement("_ >= 10")
        int s = 100;
        s++;
        @repair.regen.specification.Refinement("_ < 0")
        int a = -6;
        @repair.regen.specification.Refinement("b > 0")
        int b = 8;
        a = -3;
        a = -(6 + 5);
        b = -a;
        b = -(-10);
        b = +3;
        b = +s;
        @repair.regen.specification.Refinement("_ <= 0")
        int c = 5 * (-10);
    }
}

