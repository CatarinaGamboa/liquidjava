

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;
import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"green", "yellow", "red"})
@RefinementAlias("RGB(int x) {x >= 0 && x <= 255}")
public class TrafficLight {

	@Refinement("RGB(r)") private int r; 	
	@Refinement("RGB(g)") private int g; 	
	@Refinement("RGB(b)") private int b;

	@StateRefinement(to="green(this)")
	public TrafficLight() {
		r = 255; g = 0; b = 0; 
	}
	@StateRefinement(from="green(this)", to="amber(this)")
	public void transitionToAmber() {
		r = 255; g = 120; b = 0;
	}

	@StateRefinement(from="ref(this)", to="green(this)")
	public void transitionToGreen() {
		r = 76; g = 187; b = 23; 
	}

	@StateRefinement(from="yellow(this)", to="red(this)")
	public void transitionToRed() {
		r = 230; g = 0; b = 1-1; 
	}

}



