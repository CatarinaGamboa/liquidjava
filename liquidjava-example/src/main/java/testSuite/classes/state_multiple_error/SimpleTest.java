package testSuite.classes.state_multiple_error;

import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleTest {

    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        isr.read();
        isr.close();
        isr.close();
    }
}
