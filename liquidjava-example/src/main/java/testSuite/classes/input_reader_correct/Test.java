package testSuite.classes.input_reader_correct;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class Test {
  public static void main(String[] args) throws Exception {
    // Arrays are not well supported in LiquidJava
    
    // InputStreamReader isr = new InputStreamReader(new FileInputStream("test1.txt"));
    // @Refinement("_ > -9")
    // int a = isr.read();
    // char[] arr = new char[20];
    // int b = isr.read(arr, 10, 5);
    // System.out.println(arr);
    // isr.close();
  }
}
