package repair.regen;


public class ErrorAfterIf {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("y < 100")
        int y = 5;
        if (y > 2)
            y = 3;
        else
            y = 9;

        @repair.regen.specification.Refinement("z < 7")
        int z = y;
    }
}

