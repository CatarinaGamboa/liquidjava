package repair.regen.api.tests;

import org.junit.Test;

public class TestMath extends TestAbstract {

    @Test
    public void correctArithmeticBinaryOperation() {
        testCorrect(testPath + "CorrectArithmeticBinaryOperations.java");
    }

    @Test
    public void correctLongUsage() {
        testCorrect(testPath + "CorrectLongUsage.java");
    }

    @Test
    public void correctPrimitiveNumbersTypes() {
        testCorrect(testPath + "CorrectPrimitiveNumbersTypes.java");
    } // Takes a long time

    @Test
    public void correctFPArithmetic() {
        testCorrect(testPath + "CorrectFPArithmetic.java");
    }

    @Test
    public void correctInvocationFromMathLibrary() {
        testCorrect(testPath + "/math/correctInvocation");
    }

    @Test
    public void errorArithmeticBinaryOperation() {
        testWrong(testPath + "ErrorArithmeticBinaryOperations.java");
    }

    @Test
    public void errorMathMax() {
        testWrong(testPath + "/math/errorMax");
    }

    @Test
    public void errorMathAbs() {
        testWrong(testPath + "/math/errorAbs");
    }

    @Test
    public void errorMathMultiplyExact() {
        testWrong(testPath + "/math/errorMultiplyExact");
    }

    @Test
    public void errorUnaryOpMinus() {
        testWrong(testPath + "ErrorUnaryOpMinus.java");
    }

    @Test
    public void errorLongUsage1() {
        testWrong(testPath + "ErrorLongUsage1.java");
    }

    @Test
    public void errorLongUsage2() {
        testWrong(testPath + "ErrorLongUsage2.java");
    }

    @Test
    public void errorArithmeticFP1() {
        testWrong(testPath + "ErrorArithmeticFP1.java");
    }

    @Test
    public void errorArithmeticFP2() {
        testWrong(testPath + "ErrorArithmeticFP2.java");
    }

    @Test
    public void errorArithmeticFP3() {
        testWrong(testPath + "ErrorArithmeticFP3.java");
    }

    @Test
    public void errorArithmeticFP4() {
        testWrong(testPath + "ErrorArithmeticFP4.java");
    }

    @Test
    public void errorSpecificArithmetic() {
        testWrong(testPath + "ErrorSpecificArithmetic.java");
    }

}
