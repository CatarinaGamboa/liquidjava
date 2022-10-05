package regen.test.project;


@liquidjava.specification.StateSet({ "green", "yellow", "red" })
@liquidjava.specification.RefinementAlias("RGB(int x) {x >= 0 && x <= 255}")
public class TrafficLight {
    @liquidjava.specification.Refinement("RGB(r)")
    private int r;

    @liquidjava.specification.Refinement("RGB(g)")
    private int g;

    @liquidjava.specification.Refinement("RGB(b)")
    private int b;

    @liquidjava.specification.StateRefinement(to = "green(this)")
    public TrafficLight() {
        r = 255;
        g = 0;
        b = 0;
    }

    @liquidjava.specification.StateRefinement(from = "green(this)", to = "amber(this)")
    public void transitionToAmber() {
        r = 255;
        g = 120;
        b = 0;
    }

    @liquidjava.specification.StateRefinement(from = "ref(this)", to = "green(this)")
    public void transitionToGreen() {
        r = 76;
        g = 187;
        b = 23;
    }

    @liquidjava.specification.StateRefinement(from = "yellow(this)", to = "red(this)")
    public void transitionToRed() {
        r = 230;
        g = 0;
        b = 1 - 1;
    }
}

