package testSuite.classes.stack_overflow.file_input_stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



/**
 * Examples related to typestate refinements.
 * Idea from: https://stackoverflow.com/questions/5900435/blackberry-fileconnection-illegalstateexception
 * I am not sure we can handle this case since testFile does not need to know about FileInputStream, it is only used as a parameter. 
 * Can we still relate them? Does it make sense?
 */
@SuppressWarnings("unused")
public class Test {
    
    public static void main(String[] args) {

        // Create a test file
        File testFile = new File("liquidjava-example/src/main/java/testSuite/classes/stack_overflow/file_input_stream/testFile.txt");

        // Simulate opening the file and forgetting to close it
        FileInputStream inputStream = null;
        try {
            // Open the file
            inputStream = new FileInputStream(testFile);
            System.out.println("Reading the file...");
            
            // Simulate a long operation without closing the file
            // Note: Normally, you'd process the file here, but we're just mimicking the issue
            Thread.sleep(500);  // Simulating a delay
            
            // We forget to close the file here, mimicking the problem
            // inputStream.close(); // Error: Should be here to fix the issue
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Attempt to delete the file (this will fail if the file is still open)
        if (testFile.delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete the file. It might be open.");
        }
    }
}
