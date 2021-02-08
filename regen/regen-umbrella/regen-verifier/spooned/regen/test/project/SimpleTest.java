package regen.test.project;


// //correctImplies -rever!!!
// @Refinement("_ > 5")
// int x = 10;
// 
// @Refinement("(x > 50) --> (y > 50)")
// int y = x;
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
        @repair.regen.specification.Refinement("(_ == -5)")
        float prim = java.lang.Math.copySign((-5), (-500));
        @repair.regen.specification.Refinement("_ == -656")
        float ter = java.lang.Math.copySign(656, prim);
    }
}

