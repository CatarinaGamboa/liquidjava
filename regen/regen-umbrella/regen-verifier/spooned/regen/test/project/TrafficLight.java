package regen.test.project;


@repair.regen.specification.StateSet({ "Green", "Amber", "Red" })
public class TrafficLight {
    @repair.regen.specification.Refinement("r >= 0 && r <= 255")
    int r;

    @repair.regen.specification.Refinement("g >= 0 && g <= 255")
    int g;

    @repair.regen.specification.Refinement("b >= 0 && b <= 255")
    int b;

    @repair.regen.specification.StateRefinement(to = "Green(this)")
    public TrafficLight() {
        r = 255;
        g = 0;
        b = 0;
    }

    @repair.regen.specification.StateRefinement(to = "Green(this)", from = "Amber(this)")
    public void transitionToAmber() {
        r = 255;
        g = 120;
        b = 0;
    }

    @repair.regen.specification.StateRefinement(to = "Red(this)", from = "Green(this)")
    public void transitionToGreen() {
        r = 76;
        g = 187;
        b = 23;
    }

    @repair.regen.specification.StateRefinement(to = "Amber(this)", from = "Red(this)")
    public void transitionToRed() {
        r = 230;
        g = 0;
        b = 0;
    }
}

