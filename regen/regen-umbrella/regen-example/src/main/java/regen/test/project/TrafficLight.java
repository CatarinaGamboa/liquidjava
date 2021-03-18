package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"green", "solidAmber", "red", "flashingAmber"})
public class TrafficLight {
	//StateRefinement -> refines the state of the present object
	//					 independently of the arguments or the return of the method
		
	@StateRefinement(to="green(this)")
	public TrafficLight() {}
	
	@StateRefinement(from="green(this)", to="solidAmber(this)")
	public void transitionToAmber() {}
	
	@StateRefinement(from="solidAmber(this)", to="red(this)")
	public void transitionToRed() {}
	
	@StateRefinement(from="red(this)", to="flashingAmber(this)")
	public void transitionToFlashingAmber() {}
	
	@StateRefinement(from="flashingAmber(this)", to="green(this)")
	public void transitionToGreen() {}
	
	@StateRefinement(from="red(this)")
	public void passagersCross() {}
	
	@StateRefinement(to = "flashingAmber(this)")
	public void intermitentMalfunction() {}
	
	
	
	
	@StateRefinement(from="green(this)", to="solidAmber(this)")
	public TrafficLight transitionToAmber2() {
		return this;
	}
	
	public TrafficLight getStartingTrafficLight() {
		return new TrafficLight();
	}
	
	@StateRefinement(to="green(this)")
	public int getTotalChangesReset() {
		return 0;//count
	}
	
	
	
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
