package repair.regen.classes;


@repair.regen.specification.StateSet({ "open", "close" })
public class MyStreamReader {
    @repair.regen.specification.StateRefinement(from = "close(this)", to = "open(this)")
    public MyStreamReader() {
    }
}

