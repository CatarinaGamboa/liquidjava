package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectDependentRefinements {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int smaller = 5;
        @liquidjava.specification.Refinement("bigger > 20")
        int bigger = 50;
        @liquidjava.specification.Refinement("_ > smaller  && _ < bigger")
        int middle = 15;
        @liquidjava.specification.Refinement("_ >= smaller")
        int k = 10;
        @liquidjava.specification.Refinement("_ <= bigger")
        int y = 10;
        @liquidjava.specification.Refinement("_ == 20")
        int x1 = 20;
        @liquidjava.specification.Refinement("_ == x1 + 1")
        int x2 = 21;
        @liquidjava.specification.Refinement("_ == x1 - 1")
        int x3 = 19;
        @liquidjava.specification.Refinement("_ == x1 * 5")
        int x4 = x1 * 5;
        @liquidjava.specification.Refinement("_ == x1 / 2")
        int x5 = 10;
        @liquidjava.specification.Refinement("_ == x1 % 2")
        int x6 = 0;
        @liquidjava.specification.Refinement("(-x7) < x1")
        int x7 = 0;
        @liquidjava.specification.Refinement("_ != x1")
        int x8 = 0;
        @liquidjava.specification.Refinement("_ == 30")
        int o = 30;
        @liquidjava.specification.Refinement("_ == x1 || _ == o ")
        int x9 = 20;
    }
}

