package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("type Positive(int x) { x > 0}")
public class Car {
	@Refinement("_ > 1900 && _ < 2050")
	private int year;
	@Refinement("Positive(_)")
	private int seats;
	
	public void setYear(@Refinement("_ > 1900 && _ < 2050") int y) {
		this.year = y;
	}
	
	@Refinement("_ == year")
	public int getYear() {
		return year;
	}
	

}
