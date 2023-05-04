package liquidjava.api.tests;

import org.junit.Test;

public class TestSeparationLogic extends TestAbstract {
    @Test
    public void correctSimpleFunctionCall() {
        testCorrect(testPath + "separation_logic/CorrectSimpleFunctionCall.java");
    }

    @Test
    public void errorSimpleFunctionCall() {
        testWrong(testPath + "separation_logic/ErrorSimpleFunctionCall.java");
    }

    @Test
    public void errorHeapShrinkAssign() {
        testWrong(testPath + "separation_logic/ErrorHeapShrinkAssign.java");
    }

    @Test
    public void correctHeapShrinkAssign() {
        testWrong(testPath + "separation_logic/CorrectHeapShrinkAssign.java");
    }

    @Test
    public void correctConstructor() {
        testCorrect(testPath + "separation_logic/CorrectConstructor.java");
    }

    @Test
    public void errorConstructor() {
        testWrong(testPath + "separation_logic/ErrorConstructor.java");
    }

    @Test
    public void errorSimpleIf() {
        testWrong(testPath + "separation_logic/ErrorSimpleIf.java");
    }

//    @Test
//    public void correctSimpleIf() {
//        testCorrect(testPath + "separation_logic/CorrectSimpleIf.java");
//    }
}
