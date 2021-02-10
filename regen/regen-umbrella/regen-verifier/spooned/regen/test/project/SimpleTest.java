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
    @repair.regen.specification.Refinement("_ > 0")
    public static int toPositive(@repair.regen.specification.Refinement("a < 0")
    int a) {
        return -a;
    }

    @repair.regen.specification.Refinement("_ < 0")
    public static int toNegative(@repair.regen.specification.Refinement("a > 0")
    int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 5;
        if (a < 0) {
            @repair.regen.specification.Refinement("b < 0")
            int b = a;
        } else {
            @repair.regen.specification.Refinement("b >= 0")
            int b = a;
        }
        // EXAMPLE 2
        @repair.regen.specification.Refinement("_ < 10")
        int ex_a = 5;
        if (ex_a < 0) {
            @repair.regen.specification.Refinement("_ >= 10")
            int ex_b = (regen.test.project.SimpleTest.toPositive(ex_a)) * 10;
        } else {
            if (ex_a != 0) {
                @repair.regen.specification.Refinement("_ < 0")
                int ex_d = regen.test.project.SimpleTest.toNegative(ex_a);
            }
            @repair.regen.specification.Refinement("_ < ex_a")
            int ex_c = -10;
        }
    }
}

