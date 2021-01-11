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
        @repair.regen.specification.Refinement("\\v < -5.5")
        double d = (-a) - 2.0;
        // 
        // @Refinement("\\v == 30")
        // int o = 30;
        // @Refinement("\\v == x1 || \\v == o ")
        // int x9 = 20;
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

