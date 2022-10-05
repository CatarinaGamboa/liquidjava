package liquidjava.classes.input_reader_correct;


public class Test {
    public static void main(java.lang.String[] args) throws java.lang.Exception {
        java.io.InputStreamReader isr = new java.io.InputStreamReader(new java.io.FileInputStream("test1.txt"));
        @liquidjava.specification.Refinement("_ > -9")
        int a = isr.read();
        char[] arr = new char[20];
        int b = isr.read(arr, 10, 5);
        java.lang.System.out.println(arr);
    }
}

