package liquidjava.separation_logic.correctfiletest;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

import java.io.FileWriter;
import java.io.IOException;

@ExternalRefinementsFor("java.io.FileWriter")
interface FileWriterConstrutor {
    FileWriter FileWriter(String str);
}

@ExternalRefinementsFor("java.io.OutputStreamWriter")
interface OutputStreamCloseAnnotations {
    @HeapPrecondition("this |-> sep.()")
    @HeapPostcondition("sep.emp")
    void close();
}

public class CorrectFileTest {
    static void run1() throws IOException {
        FileWriter f = new FileWriter("input.txt");
        f.close();
    }

    static void run2() throws IOException {
        FileWriter f = new FileWriter("input.txt");
        consumeFile(f);
    }

    @HeapPrecondition("file |-> sep.()")
    @HeapPostcondition("sep.emp")
    static void consumeFile(FileWriter file) {

    }
}

