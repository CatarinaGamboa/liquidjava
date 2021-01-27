package regen.test.project;


// Errors to take care of
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
        @repair.regen.specification.Refinement("y < 100")
        int y = 50;
        if (y > 2)
            y = 3;
        else
            y = 6;

        @repair.regen.specification.Refinement("z < 7")
        int z = y;
        // see error
        // @Refinement("\\v < 100")
        // int ielse = 90;
        // 
        // @Refinement("\\v < 10")
        // int then = 7;
        // if(then > 6)
        // then = then-8;
        // else
        // ielse = 5;
        // 
        // @Refinement("\\v == 7 || \\v == 5")
        // int some = then;
        // @Refinement("\\v == 7 || \\v==-1")
        // int thing = changedInElse;
    }
}

