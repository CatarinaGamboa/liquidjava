package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectFunctionCallSameVariableName {
    @liquidjava.specification.Refinement(" _ == a + 1")
    private static int addOnes(@liquidjava.specification.Refinement("a > 20")
    int a) {
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 0")
        int a = 6;
        @liquidjava.specification.Refinement("_ > 20")
        int b = (liquidjava.CorrectFunctionCallSameVariableName.addOnes(50)) + a;
        @liquidjava.specification.Refinement("_ > 10")
        int c = liquidjava.CorrectFunctionCallSameVariableName.addOnes((a + 90));
        @liquidjava.specification.Refinement("_ < 0")
        int d = -(liquidjava.CorrectFunctionCallSameVariableName.addOnes((a + 90)));
        @liquidjava.specification.Refinement("_ > 0")
        int e = liquidjava.CorrectFunctionCallSameVariableName.addOnes((a + 100));
    }
}

