package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("Positive(int x) { x > 0}")
@RefinementAlias("type CarAcceptableYears(int x) { x > 1800 && _ < 2050}")
public class Car {
	@Refinement("CarAcceptableYears(_)")
	private int year;
	@Refinement("Positive(_)")
	private int seats;
	
	public void setYear(@Refinement("CarAcceptableYears(_)") int year) {
		this.year = year;
	}
	
	@Refinement("_ == year")
	public int getYear() {
		return year;
	}
	
//	@Refinement("_ == year")
//	public int getAge(int yearNow) {
//		return (yearNow+1) - year;
//	}
	

}
