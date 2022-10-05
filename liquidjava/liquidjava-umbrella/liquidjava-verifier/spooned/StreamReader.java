

// public static void main(String[] args) throws Exception{
// StreamReader sr = new StreamReader();
// sr.read();
// sr.close();
// // sr.read();
// InputStreamReader isr = new InputStreamReader(null);
// isr.read();
// isr.close();
// // isr.read();
// }
@liquidjava.specification.StateSet({ "myOpen", "myClosed" })
public class StreamReader {
    @liquidjava.specification.StateRefinement(to = "myOpen(this)")
    public StreamReader() {
    }

    @liquidjava.specification.StateRefinement(from = "myOpen(this)")
    public void read() {
    }

    @liquidjava.specification.StateRefinement(from = "myOpen(this)", to = "myClosed(this)")
    public void close() {
    }
}

