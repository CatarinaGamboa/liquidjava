package liquidjava.classes.input_reader_error2;


public class Test {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        java.io.InputStreamReader isr = new java.io.InputStreamReader(java.lang.System.in);
        isr.read();
        isr.close();
        isr.getEncoding();
        isr.read();
    }
}

