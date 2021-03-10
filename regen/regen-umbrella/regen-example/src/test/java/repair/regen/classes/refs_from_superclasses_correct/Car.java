package repair.regen.classes.refs_from_superclasses_correct;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public abstract class Car {

	public abstract void setYear(@Refinement("IntBetween(y, 1600, 2050)")int y); 
	
	@Refinement("_ >= 1 && _ < ceil")
	public abstract int getSeats(@Refinement("_ == 50")int ceil);

}
