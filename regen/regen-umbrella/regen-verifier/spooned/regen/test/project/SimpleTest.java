package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a < 0 }->{\\v > 0}")
    public static int toPositive(int a) {
        return -a;
    }

    @repair.regen.specification.Refinement("{a > 0 }->{\\v < 0}")
    public static int toNegative(int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int ex_a = 5;
        if (ex_a < 0) {
            @repair.regen.specification.Refinement("\\v >= 10")
            int ex_b = (regen.test.project.SimpleTest.toPositive(ex_a)) * 10;
        } else {
            if (ex_a != 0) {
                @repair.regen.specification.Refinement("\\v < 0")
                int ex_d = regen.test.project.SimpleTest.toNegative(ex_a);
            }
            @repair.regen.specification.Refinement("\\v < ex_a")
            int ex_c = -10;
        }
        // //SHOULD BE ERROR -inconsitency a < 0 and a == 100 -> False prove->True
        // @Refinement("\\v < 10")
        // int a = 5;
        // if(a < 0)
        // a = 100;
    }
}

