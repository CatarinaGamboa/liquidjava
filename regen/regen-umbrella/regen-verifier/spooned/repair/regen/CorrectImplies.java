package repair.regen;


public class CorrectImplies {
    @repair.regen.specification.Refinement("{true}->{((arg0 < 0) --> (\\v == (-arg0*2))) && ((arg0 >= 0) --> (\\v == arg0*2))}")
    private static int getPositiveDouble(int arg0) {
        if (arg0 < 0)
            return (-arg0) * 2;
        else
            return arg0 * 2;

    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5")
        int x = 10;
        @repair.regen.specification.Refinement("(x > 50) --> (y > 50)")
        int y = x;
        @repair.regen.specification.Refinement("y > 1 --> z > 2")
        int z = y * 2;
        @repair.regen.specification.Refinement("\\v == 12")
        int z0 = repair.regen.CorrectImplies.getPositiveDouble(6);
    }
}

