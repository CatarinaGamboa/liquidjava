package repair.regen.classes.iterator_error;


@repair.regen.specification.StateSet({ "notready", "ready", "finished" })
public class Iterator {
    boolean hn;

    @repair.regen.specification.StateRefinement(from = "notready(this)", to = "ready(this)")
    boolean hasNext(boolean hn) {
        return hn;
    }

    @repair.regen.specification.StateRefinement(from = "ready(this)", to = "finished(this)")
    int next(boolean hn) {
        int r;
        if (hn)
            r = 1;
        else
            r = -1;

        return r;
    }
}

