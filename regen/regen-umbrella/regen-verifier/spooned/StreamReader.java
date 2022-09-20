

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
@repair.regen.specification.StateSet({ "myOpen", "myClosed" })
public class StreamReader {
    @repair.regen.specification.StateRefinement(to = "myOpen(this)")
    public StreamReader() {
    }

    @repair.regen.specification.StateRefinement(from = "myOpen(this)")
    public void read() {
    }

    @repair.regen.specification.StateRefinement(from = "myOpen(this)", to = "myClosed(this)")
    public void close() {
    }
}

