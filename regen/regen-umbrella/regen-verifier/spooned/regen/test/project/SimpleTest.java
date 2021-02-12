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
    // 
    @repair.regen.specification.Refinement("_ == 3")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        a = (a == 2) ? 6 + (regen.test.project.SimpleTest.three()) : 4 * (regen.test.project.SimpleTest.three());
    }
}

