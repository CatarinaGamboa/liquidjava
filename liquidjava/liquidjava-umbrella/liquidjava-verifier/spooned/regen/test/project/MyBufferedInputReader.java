package regen.test.project;


@liquidjava.specification.StateSet({ "empty", "filled" })
public class MyBufferedInputReader extends regen.test.project.MyInputStreamReader {
    @liquidjava.specification.StateRefinement(to = "filled(this) && open(this)")
    public MyBufferedInputReader() {
    }

    // @StateRefinement(from="open(this)", to="open(this)")
    @liquidjava.specification.StateRefinement(from = "open(this) && filled(this)", to = "open(this)")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read() {
        return 0;
    }
}

