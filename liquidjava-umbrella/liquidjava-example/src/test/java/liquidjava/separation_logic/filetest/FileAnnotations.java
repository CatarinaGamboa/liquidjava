package liquidjava.separation_logic.filetest;

import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.HeapPostcondition;
import liquidjava.specification.HeapPrecondition;

@ExternalRefinementsFor("java.io.FileWriter")
public interface FileAnnotations{
    void FileWriter(String str);
    @HeapPrecondition("this |-> sep.()")
    @HeapPostcondition("sep.emp")
    void close();

}

