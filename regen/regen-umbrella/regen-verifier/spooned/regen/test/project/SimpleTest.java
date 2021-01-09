package regen.test.project;


// @Refinement("\\v < 10")
// int a = 6;
// 
// if(a > 3) {
// a = 7 + 1;
// }//else {
// @Refinement("b > 8")
// int b = a;
// 
// }
// @Refinement("b > 8")
// int b = a;
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
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

