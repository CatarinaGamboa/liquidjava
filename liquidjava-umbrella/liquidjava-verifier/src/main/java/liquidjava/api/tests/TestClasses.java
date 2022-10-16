package liquidjava.api.tests;

import org.junit.Test;

public class TestClasses extends TestAbstract {

    @Test
    public void correctEmail1() {
        testCorrect(testPath + "/classes/email1");
    }

    @Test
    public void correctStateFromSuperclass() {
        testCorrect(testPath + "/classes/state_from_superclass_correct");
    }

    @Test
    public void correctStateCases() {
        testCorrect(testPath + "/classes/state_multiple_cases");
    }

    @Test
    public void correctSimpleCarTest() {
        testCorrect(testPath + "/classes/car1");
    }

    @Test
    public void correctCheckRefinementsSupertype() {
        testCorrect(testPath + "/classes/refs_from_superclasses_correct");
    }

    @Test
    public void correctArrayListSize() {
        testCorrect(testPath + "/classes/arraylist_correct");
    }

    @Test
    public void correctTrafficLights1() {
        testCorrect(testPath + "/classes/traffic_light_1");
    }

    @Test
    public void correctOrderGift() {
        testCorrect(testPath + "/classes/order_gift_correct");
    }

    @Test
    public void correctIterator() {
        testCorrect(testPath + "/classes/iterator_correct");
    }

    // @Test
    // public void correctInputReaderCharArrayLongerNameEve() {
    // testCorrect(testPath+"/classes/input_reader_correct");
    // }
    //

    @Test
    public void errorInputReader() {
        testWrong(testPath + "/classes/input_reader_error");
    }

    @Test
    public void errorInputReader2() {
        testWrong(testPath + "/classes/input_reader_error2");
    }

    @Test
    public void errorStatesSameSet() {
        testWrong(testPath + "/classes/state_multiple_error");
    }

    @Test
    public void errorConstructorWithFromCase() {
        testWrong(testPath + "/classes/MyStreamReader.java");
    }

    @Test
    public void errorEmail2() {
        testWrong(testPath + "/classes/email2");
    }

    @Test
    public void errorEmail3() {
        testWrong(testPath + "/classes/email3");
    }

    @Test
    public void errorRefsFromInterface() {
        testWrong(testPath + "/classes/refs_from_interface_error");
    }

    @Test
    public void errorRefsFromSuperclass() {
        testWrong(testPath + "/classes/refs_from_superclass_error");
    }

    @Test
    public void errorOrderGiftError() {
        testWrong(testPath + "/classes/order_gift_error");
    }

    @Test
    public void errorGhostState() {
        testWrong(testPath + "/classes/ErrorGhostState.java");
    }

    @Test
    public void errorSocket() {
        testWrong(testPath + "/classes/socket_error");
    }

    @Test
    public void errorIterator() {
        testWrong(testPath + "/classes/iterator_error");
    }

}
