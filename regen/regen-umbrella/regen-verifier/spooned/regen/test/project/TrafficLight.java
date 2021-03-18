package regen.test.project;


// @StateRefinement(from="green(this)", to="solidAmber(this)")
// @StateRefinement(from="solidAmber(this)", to="red(this)")
// @StateRefinement(from="red(this)", to="flashingAmber(this)")
// @StateRefinement(from="flashingAmber(this)", to="green(this)")
// public void transition() {}
// 
// @Refinement("_ == green(this)")
// public boolean carsPass() {
// return true;
// }
@repair.regen.specification.StateSet({ "green", "solidAmber", "red", "flashingAmber" })
public class TrafficLight {
    @repair.regen.specification.StateRefinement(to = "green(this)")
    public TrafficLight() {
    }

    @repair.regen.specification.StateRefinement(from = "green(this)", to = "solidAmber(this)")
    public regen.test.project.TrafficLight transitionToAmber2() {
        return this;
    }

    public regen.test.project.TrafficLight getStartingTrafficLight() {
        return new regen.test.project.TrafficLight();
    }

    @repair.regen.specification.StateRefinement(from = "green(this)", to = "solidAmber(this)")
    public void transitionToAmber() {
    }

    @repair.regen.specification.StateRefinement(from = "solidAmber(this)", to = "red(this)")
    public void transitionToRed() {
    }

    @repair.regen.specification.StateRefinement(from = "red(this)", to = "flashingAmber(this)")
    public void transitionToFlashingAmber() {
    }

    @repair.regen.specification.StateRefinement(from = "flashingAmber(this)", to = "green(this)")
    public void transitionToGreen() {
    }

    @repair.regen.specification.StateRefinement(from = "red(this)")
    public boolean passagersCanCross() {
        return true;
    }

    @repair.regen.specification.StateRefinement(to = "flashingAmber(this)")
    public void intermitentMalfunction() {
    }
}

