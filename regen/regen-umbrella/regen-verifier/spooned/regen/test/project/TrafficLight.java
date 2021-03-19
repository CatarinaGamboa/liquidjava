package regen.test.project;


// @StateRefinement(from="red(this)")
// public void passagersCross() {}
// 
// @StateRefinement(to = "flashingAmber(this)")
// public void intermitentMalfunction() {}
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
@repair.regen.specification.StateSet({ "a", "b" })
public class TrafficLight {
    // StateRefinement -> refines the state of the present object
    // independently of the arguments or the return of the method
    @repair.regen.specification.StateRefinement(to = "green(this) && (a(this) || b(this))")
    public TrafficLight() {
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

    @repair.regen.specification.Refinement("red(_)")
    public regen.test.project.TrafficLight getTrafficLightStartingRed() {
        regen.test.project.TrafficLight t = new regen.test.project.TrafficLight();
        t.transitionToAmber();
        t.transitionToRed();
        return t;
    }

    // @StateRefinement(from="green(this)", to="solidAmber(this)")
    // @Refinement("this == _")
    // public TrafficLight transitionToAmber2() {
    // //...
    // return this;
    // }
    @repair.regen.specification.StateRefinement(to = "green(this)")
    @repair.regen.specification.Refinement("_ >= 0")
    public int getTotalChangesReset() {
        return 0;// count

    }
}

