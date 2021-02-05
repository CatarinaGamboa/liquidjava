package regen.test.project;


// Errors to take care of
// //value_4==innerScope && value_4 == innerScope_1
// @Refinement("_ < 100")
// int value = 90;
// 
// if(value > 6) {
// @Refinement("_ > 10")
// int innerScope = 30;
// value = innerScope;
// }
// 
// @Refinement("_ == 30 || _ == 90")
// int some2 = value;
// SEE ERROR still error
// @Refinement("(_ == -5)")
// float prim = Math.copySign(-5, -500);
// @Refinement("_ == -656")
// float ter = Math.copySign(656, prim);
// See error NaN
// @Refinement("true")
// double b = 0/0;
// @Refinement("_ > 5")
// double c = b;
// @Refinement("true")
// int a = 10;
// int b = (a < 100)? three(): three()-1;
// @Refinement("c < 100")
// int c = (a < 100)? three(): a;
// c = (a < 100)? three()*3 : a*5;
public class SimpleTest {
    // @Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    // public static int posMult(int a, int b) {
    // @Refinement("y > 30")
    // int y = 50;
    // return y-10;
    // }
    // 
    // @Refinement("{_ == 10}")
    // public static int ten() {
    // return 10;
    // }
    // 
    @repair.regen.specification.Refinement("{true}->{_ == b*2}")
    private static int multTwo(int b) {
        return b * 2;
    }

    public static void main(java.lang.String[] args) {
        // @Refinement("_ >= 0")
        // int p = 10;
        // p = posMult(ten(), 4);
        // 
        @repair.regen.specification.Refinement("_ < 6")
        int z = 5;
        @repair.regen.specification.Refinement("_ > 6")
        int x = regen.test.project.SimpleTest.multTwo(z);
        // @Refinement("_ == 20")
        // int y = multTwo(x);
    }
}

