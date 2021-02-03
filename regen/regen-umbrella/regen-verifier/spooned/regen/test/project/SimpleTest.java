package regen.test.project;


// Errors to take care of
// //value_4==innerScope && value_4 == innerScope_1
// @Refinement("\\v < 100")
// int value = 90;
// 
// if(value > 6) {
// @Refinement("\\v > 10")
// int innerScope = 30;
// value = innerScope;
// }
// 
// @Refinement("\\v == 30 || \\v == 90")
// int some2 = value;
// SEE ERROR still error
// @Refinement("(\\v == -5)")
// float prim = Math.copySign(-5, -500);
// @Refinement("\\v == -656")
// float ter = Math.copySign(656, prim);
// See error NaN
// @Refinement("true")
// double b = 0/0;
// @Refinement("\\v > 5")
// double c = b;
// @Refinement("true")
// int a = 10;
// int b = (a < 100)? three(): three()-1;
// @Refinement("c < 100")
// int c = (a < 100)? three(): a;
// c = (a < 100)? three()*3 : a*5;
public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("\\v == 10.0")
        double b = 10.0;
        @repair.regen.specification.Refinement("\\v != 10.0")
        double c = 5.0;
        @repair.regen.specification.Refinement("t > 0.0")
        double t = a + 1.0;
        @repair.regen.specification.Refinement("\\v >= 3.0")
        double k = a - 1.0;
        @repair.regen.specification.Refinement("\\v > 0.0")
        double l = k * t;
        @repair.regen.specification.Refinement("\\v > 0.0")
        double m = l / 2.0;
        @repair.regen.specification.Refinement("\\v < 4.0")
        double n = 6.0 % 4.0;
        @repair.regen.specification.Refinement("\\v < 0.0")
        double p = -5.0;
        @repair.regen.specification.Refinement("\\v <= 0.0")
        double p1 = -a;
        @repair.regen.specification.Refinement("\\v < -1.0")
        double p3 = p;
        @repair.regen.specification.Refinement("\\v < -5.5")
        double d = (-a) - 2.0;
    }
}

