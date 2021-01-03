package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a > 0} -> {true}")
    public static void addZ(int a) {
        @repair.regen.specification.Refinement("\\v > 0")
        int d = a;
        if (d > 5) {
            @repair.regen.specification.Refinement("b > 5")
            int b = d;
        } else {
            @repair.regen.specification.Refinement("\\v <= 5")
            int c = d;
            d = 10;
            @repair.regen.specification.Refinement("b > 9")
            int b = d;
        }
    }

    public static void main(java.lang.String[] args) {
        // @Refinement("\\v > 10")
        // int a = 15;
        // if(a > 14) {
        // @Refinement("\\v > 14")
        // int b = a;
        // a = 12;
        // @Refinement("\\v < 13")
        // int c = a;
        // }
    }
}

