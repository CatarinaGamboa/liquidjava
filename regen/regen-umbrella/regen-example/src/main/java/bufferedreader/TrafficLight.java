package bufferedreader;

import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"green", "amber", "red"})
public class TrafficLight {

	public TrafficLight() {}
	
	@StateRefinement(from="red(this)", to="green(this)")
	public void transitionToGreen() {}
	
	@StateRefinement(from="red(this)", to="amber(this)")
	@StateRefinement(from="green(this)", to="amber(this)")
	public void transitionToAmber() {}
	
	@StateRefinement(from="amber(this)", to="red(this)")
	public void transitionToRed() {}
	

	
	
	

	
	
	
	
	
	
	
	
	
//	@StateRefinement(from="red(this)", to="flashingAmber(this)")
//	public void transitionToFlashingAmber() {}
//	
	
//	@Refinement("red(_)")
//	public TrafficLight getTrafficLightStartingRed() {
//		TrafficLight t = new TrafficLight();
//		t.transitionToAmber();
//		t.transitionToRed();
//		return t;
//	}

	
//	@StateRefinement(from="green(this)", to="solidAmber(this)")
//	@Refinement("this == _")
//	public TrafficLight transitionToAmber2() {
//		//...
//		return this;
//	}
	
	
	

//	
//	@StateRefinement(to="green(this)")
//	@Refinement("_ >= 0")
//	public int getTotalChangesReset() {
//		return 0;//count
//	}
	
	
//	@StateRefinement(from="red(this)")
//	public void passagersCross() {}
//	
//	@StateRefinement(to = "flashingAmber(this)")
//	public void intermitentMalfunction() {}
	
	
//	@StateRefinement(from="green(this)", to="solidAmber(this)")
//	@StateRefinement(from="solidAmber(this)", to="red(this)")
//	@StateRefinement(from="red(this)", to="flashingAmber(this)")
//	@StateRefinement(from="flashingAmber(this)", to="green(this)")
//	public void transition() {}
//	
//	@Refinement("_ == green(this)")
//	public boolean carsPass() {
//		return true;
//	}
	

}
