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
    public static void addZ(@repair.regen.specification.Refinement("a > 0")
    int a) {
        @repair.regen.specification.Refinement("_ > 0")
        int d = a;
        if (d > 5) {
            @repair.regen.specification.Refinement("b > 5")
            int b = d;
        } else {
            @repair.regen.specification.Refinement("_ <= 5")
            int c = d;
            d = 10;
            @repair.regen.specification.Refinement("b > 10")
            int b = d;
        }
    }
}

