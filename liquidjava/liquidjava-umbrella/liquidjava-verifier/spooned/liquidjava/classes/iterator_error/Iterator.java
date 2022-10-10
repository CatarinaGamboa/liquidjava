package liquidjava.classes.iterator_error;


@liquidjava.specification.StateSet({ "notready", "ready", "finished" })
public class Iterator {
    boolean hn;

    @liquidjava.specification.StateRefinement(from = "notready(this)", to = "ready(this)")
    boolean hasNext(boolean hn) {
        return hn;
    }

    @liquidjava.specification.StateRefinement(from = "ready(this)", to = "finished(this)")
    int next(boolean hn) {
        int r;
        if (hn)
            r = 1;
        else
            r = -1;

        return r;
    }
}

