package repair.regen.classes.refs_from_superclass_error;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public class Car {

	public void setYear(@Refinement("IntBetween(_, 1600, 2050)")int x) {};

}
