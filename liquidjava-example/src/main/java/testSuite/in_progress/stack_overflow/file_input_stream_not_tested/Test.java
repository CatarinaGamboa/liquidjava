package testSuite.in_progress.stack_overflow.file_input_stream_not_tested;

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
 * 
 * In a way similar to https://stackoverflow.com/questions/15609033/causes-for-illegalstateexception
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

            Thread.sleep(500);  // Simulating a delay
            
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
