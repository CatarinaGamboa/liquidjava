package testSuite.classes.input_reader_correct_default_this;

import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) throws Exception {
        InputStreamReader is = new InputStreamReader(System.in);
        is.read();
        is.read();
        is.close();
    }
}
