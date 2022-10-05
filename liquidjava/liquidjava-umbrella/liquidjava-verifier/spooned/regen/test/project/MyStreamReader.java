package regen.test.project;


@liquidjava.specification.StateSet({ "open", "close" })
public class MyStreamReader {
    @liquidjava.specification.StateRefinement(from = "close(this)", to = "open(this)")
    public MyStreamReader() {
    }
}

