package regen.test.project;


// //correctImplies -rever!!!
// @Refinement("_ > 5")
// int x = 10;
// 
// @Refinement("(x > 50) --> (y > 50)")
// int y = x;
// See error NaN
// @Refinement("true")
// double b = 0/0;
// @Refinement("_ > 5")
// double c = b;
public class SimpleTest {
    @repair.regen.specification.Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @repair.regen.specification.Refinement("{_ == 10}")
    public static int ten() {
        return 10;
    }

    @repair.regen.specification.Refinement("{true}->{_ == b*2}")
    private static int multTwo(int b) {
        return b * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ >= 0")
        int p = 10;
        p = regen.test.project.SimpleTest.posMult(regen.test.project.SimpleTest.ten(), 4);
        @repair.regen.specification.Refinement("_ < 6")
        int z = 5;
        @repair.regen.specification.Refinement("_ > 6")
        int x = regen.test.project.SimpleTest.multTwo(z);
        @repair.regen.specification.Refinement("_ == 20")
        int y = regen.test.project.SimpleTest.multTwo(x);
    }
}

