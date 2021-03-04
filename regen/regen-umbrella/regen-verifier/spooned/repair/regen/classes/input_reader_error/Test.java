package repair.regen.classes.input_reader_error;


public class Test {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        // java.io.InputStreamReader.InputStreamReader
        java.io.InputStreamReader is = new java.io.InputStreamReader(java.lang.System.in);
        is.read();
        is.read();
        is.close();
        is.read();// should not be able to read

    }
}

