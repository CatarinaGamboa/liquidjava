package bufferedreader;


@repair.regen.specification.StateSet({ "empty", "filled" })
public class MyBufferedInputReader extends bufferedreader.MyInputStreamReader {
    @repair.regen.specification.StateRefinement(to = "empty(this) && open(this)")
    public MyBufferedInputReader() {
        // TODO Auto-generated constructor stub
    }

    // @StateRefinement(from="open(this) && filled(this)", to="open(this)")
    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @repair.regen.specification.Refinement("(_ >= 0) && (_ <= 127)")
    public int read() {
        return 0;
    }
}

