package liquidjava.separation_logic.filetest;

import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

import liquidjava.separation_logic.filetest.FileAnnotations;

import java.io.FileWriter;
import java.io.IOException;

public class ErrorFileTest {
    static void run() throws IOException {
        FileWriter f = new FileWriter("input.txt");
        f.close();
        consumeFile(f);
    }

    @HeapPrecondition("file |-> sep.()")
    @HeapPostcondition("sep.emp")
    static void consumeFile(FileWriter file) {

    }
}
