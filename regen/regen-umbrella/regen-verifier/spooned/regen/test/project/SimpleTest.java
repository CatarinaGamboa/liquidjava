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
    public static void main(java.lang.String[] args) {
        // Example 1
        @repair.regen.specification.Refinement("_ < 100")
        int ielse = 90;
        @repair.regen.specification.Refinement("_ < 10")
        int then = 7;
        if (then > 6)
            then = then - 8;
        else
            ielse = 5;

        @repair.regen.specification.Refinement("_ == 7 || _ == 5")
        int some = then;
        @repair.regen.specification.Refinement("_ == 5 || _==90")
        int thing = ielse;
    }
}

