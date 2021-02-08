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
// @Refinement("true")
// int a = 10;
// int b = (a < 100)? three(): three()-1;
// @Refinement("c < 100")
// int c = (a < 100)? three(): a;
// c = (a < 100)? three()*3 : a*5;
public class SimpleTest {
    @repair.regen.specification.Refinement("{_ == 3}")
    public static int three() {
        return 3;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        @repair.regen.specification.Refinement("_ > 0")
        int b = 3;
        a = (a == 2) ? 6 : 9;
        a = (b > 2) ? 8 : -1;
        b = (a < 100) ? regen.test.project.SimpleTest.three() : (regen.test.project.SimpleTest.three()) - 1;
        @repair.regen.specification.Refinement("c < 100")
        int c = (a < 100) ? regen.test.project.SimpleTest.three() : a;
        c = (a < 100) ? (regen.test.project.SimpleTest.three()) * 3 : a * 5;
    }
}

