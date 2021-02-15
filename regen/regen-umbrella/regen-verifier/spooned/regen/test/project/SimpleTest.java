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
    // @Refinement("_ >= 0 && _ >= n")
    // public static int absolute(int n) {
    // if(0 <= n)
    // return n;
    // else
    // return 0 - n;
    // 
    // }
    @repair.regen.specification.RefinementFunction("ghost boolean open(int)")
    @repair.regen.specification.Refinement("open(1, 2) == true")
    public int add() {
        return 1;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 5")
        int a = 10;
        // CHECK
        // @Refinement("i >= 10")
        // int i = sum(10);
    }
}

