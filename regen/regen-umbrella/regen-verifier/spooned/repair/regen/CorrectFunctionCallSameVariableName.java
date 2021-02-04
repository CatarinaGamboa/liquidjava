package repair.regen;


public class CorrectFunctionCallSameVariableName {
    @repair.regen.specification.Refinement("{a > 20}->{ _ == a + 1}")
    private static int addOnes(int a) {
        return a + 1;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 0")
        int a = 6;
        @repair.regen.specification.Refinement("_ > 20")
        int b = (repair.regen.CorrectFunctionCallSameVariableName.addOnes(50)) + a;
        @repair.regen.specification.Refinement("_ > 10")
        int c = repair.regen.CorrectFunctionCallSameVariableName.addOnes((a + 90));
        @repair.regen.specification.Refinement("_ < 0")
        int d = -(repair.regen.CorrectFunctionCallSameVariableName.addOnes((a + 90)));
        @repair.regen.specification.Refinement("_ > 0")
        int e = repair.regen.CorrectFunctionCallSameVariableName.addOnes((a + 100));
    }
}

