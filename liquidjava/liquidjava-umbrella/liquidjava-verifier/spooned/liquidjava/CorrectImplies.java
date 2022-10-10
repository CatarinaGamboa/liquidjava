package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectImplies {
    @liquidjava.specification.Refinement("((arg0 < 0) --> (_ == (-arg0*2))) && ((arg0 >= 0) --> (_ == arg0*2))")
    private static int getPositiveDouble(int arg0) {
        if (arg0 < 0)
            return (-arg0) * 2;
        else
            return arg0 * 2;

    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 5")
        int x = 10;
        // @Refinement("(x > 50) --> (y > 50)")
        // int y = x;
        // 
        // @Refinement("y > 1 --> z > 2")
        // int z = y*2;
        @liquidjava.specification.Refinement("_ == 12")
        int z0 = liquidjava.CorrectImplies.getPositiveDouble(6);
        // @Refinement("z > 0 --> _ > 0") //works but takes 1min to run
        // int z1 = getPositiveDouble(z);
    }
}

