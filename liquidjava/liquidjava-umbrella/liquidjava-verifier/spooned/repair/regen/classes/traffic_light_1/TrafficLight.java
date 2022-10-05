package liquidjava.classes.traffic_light_1;


// @StateRefinement(from="green(this)", to="solidAmber(this)")
// @Refinement("this == _")
// public TrafficLight transitionToAmber2() {
// //...
// return this;
// }
// @StateRefinement(to="green(this)")
// @Refinement("_ >= 0")
// public int getTotalChangesReset() {
// return 0;//count
// }
// 
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
@liquidjava.specification.StateSet({ "green", "solidAmber", "red", "flashingAmber" })
@liquidjava.specification.StateSet({ "buttonTouched", "buttonNotTouched" })
public class TrafficLight {
    // StateRefinement -> refines the state of the present object
    // independently of the arguments or the return of the method
    public TrafficLight() {
    }

    @liquidjava.specification.StateRefinement(from = "green(this)", to = "solidAmber(this)")
    public void transitionToAmber() {
    }

    @liquidjava.specification.StateRefinement(from = "solidAmber(this)", to = "red(this)")
    public void transitionToRed() {
    }

    @liquidjava.specification.StateRefinement(from = "red(this)", to = "flashingAmber(this)")
    public void transitionToFlashingAmber() {
    }

    @liquidjava.specification.StateRefinement(from = "flashingAmber(this)", to = "green(this)")
    public void transitionToGreen() {
    }

    @liquidjava.specification.Refinement("red(_)")
    public liquidjava.classes.traffic_light_1.TrafficLight getTrafficLightStartingRed() {
        liquidjava.classes.traffic_light_1.TrafficLight t = new liquidjava.classes.traffic_light_1.TrafficLight();
        t.transitionToAmber();
        t.transitionToRed();
        return t;
    }
}

