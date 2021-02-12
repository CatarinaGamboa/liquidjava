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
    // @RefinementFunction("ghost int len(int, int, String)")
    // public static int seven() {
    // return 7;
    // }
    @repair.regen.specification.Refinement("_ >= 0 && _ >= n")
    public static int sum(int n) {
        if (n <= 0)
            return 0;
        else {
            int t1 = regen.test.project.SimpleTest.sum((n - 1));
            return n + t1;
        }
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("i >= 10")
        int i = regen.test.project.SimpleTest.sum(10);
    }
}

