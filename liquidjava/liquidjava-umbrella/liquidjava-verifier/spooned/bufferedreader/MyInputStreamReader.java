package bufferedreader;


@liquidjava.specification.StateSet({ "open", "close" })
public class MyInputStreamReader {
    @liquidjava.specification.StateRefinement(to = "open(this)")
    public MyInputStreamReader() {
    }

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read() {
        return 0;
    }

    @liquidjava.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @liquidjava.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close() {
    }
}

