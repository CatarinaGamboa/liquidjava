package liquidjava;


public class ErrorIfAssignment2 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        if (a < 0)
            a = 100;

    }
}
