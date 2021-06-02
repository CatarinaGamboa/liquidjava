package regen.test.project;


@repair.regen.specification.StateSet({ "green", "yellow", "red" })
@repair.regen.specification.RefinementAlias("RGB(int x) {x >= 0 && x <= 255}")
public class TrafficLight {
    @repair.regen.specification.Refinement("RGB(r)")
    private int r;

    @repair.regen.specification.Refinement("RGB(g)")
    private int g;

    @repair.regen.specification.Refinement("RGB(b)")
    private int b;

    @repair.regen.specification.StateRefinement(to = "green(this)")
    public TrafficLight() {
        r = 255;
        g = 0;
        b = 0;
    }

    @repair.regen.specification.StateRefinement(from = "green(this)", to = "amber(this)")
    public void transitionToAmber() {
        r = 255;
        g = 120;
        b = 0;
    }

    @repair.regen.specification.StateRefinement(from = "ref(this)", to = "green(this)")
    public void transitionToGreen() {
        r = 76;
        g = 187;
        b = 23;
    }

    @repair.regen.specification.StateRefinement(from = "yellow(this)", to = "red(this)")
    public void transitionToRed() {
        r = 230;
        g = 0;
        b = 1 - 1;
    }
}

