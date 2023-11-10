package liquidjava.separation_logic.errorfiletest;

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
