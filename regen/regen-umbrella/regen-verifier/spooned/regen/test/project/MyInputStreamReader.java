package regen.test.project;


@repair.regen.specification.StateSet({ "open", "close" })
public class MyInputStreamReader {
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public MyInputStreamReader() {
    }

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read() {
        return 0;
    }

    @repair.regen.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close() {
    }
}

