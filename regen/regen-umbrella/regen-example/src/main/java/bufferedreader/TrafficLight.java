package bufferedreader;

import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"Green", "Amber", "Red"})
public class TrafficLight {
	@Refinement("r >= 0 && r <= 255")
	int r;
	@Refinement("g >= 0 && g <= 255")
	int g; 	
	@Refinement("b >= 0 && b <= 255")
	int b;
	
	
	@StateRefinement(to="Green(this)")
	public TrafficLight() {
		r = 255; g = 0; b = 0; 
	}
	
	@StateRefinement(to="Green(this)", from="Amber(this)")
	public void transitionToAmber() {
		r = 255; g = 120; b = 0;
	}

	@StateRefinement(to="Red(this)", from="Green(this)")
	public void transitionToGreen() {
		r = 76; g = 187; b = 23; 
	}

	@StateRefinement(to="Amber(this)", from="Red(this)")
	public void transitionToRed() {
		r = 230; g = 0; b = 0; 
	}

}
