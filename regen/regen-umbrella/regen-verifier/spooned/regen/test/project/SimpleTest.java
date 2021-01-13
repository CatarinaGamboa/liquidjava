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
        @repair.regen.specification.Refinement("b < 0")
        int b = java.lang.Math.abs(6);
        // @Refinement("b > 0")
        // int b = Math.addExact(6, 2);
        // b = (a < 100)? three(): three()-1;
        // @Refinement("c < 100")
        // int c = (a < 100)? three(): a;
        // c = (a < 100)? three()*3 : a*5;
    }
}

