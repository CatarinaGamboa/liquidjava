package testSuite.classes.scoreboard_error;

import liquidjava.specification.Ghost;
import liquidjava.specification.StateRefinement;

@Ghost("double value")
public class Scoreboard {

    @StateRefinement(from = "value(this) < 1.0", to = "value(this) == value(old(this)) + 0.1")
    public void inc() {}

    @StateRefinement(from = "value(this) > 0.0", to = "value(this) == value(old(this)) - 0.1")
    public void dec() {}

    @StateRefinement(from = "value(this) > 0.0")
    public void finish() {}
}