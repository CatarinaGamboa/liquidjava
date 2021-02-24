package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("Positive(int x) { x > 0}")
@RefinementAlias("type CarAcceptableYears(int x) { x > 1800 && x < 2050}")
public class Car {
	
	@Refinement("CarAcceptableYears(year)")
	private int year;
	
	@Refinement("Positive(_)")
	private int seats;
	
	
	public void setYear(@Refinement(" _ > 1900 && _ < 2000") int year) {
		this.year = year;
	}
	
	@Refinement("CarAcceptableYears(_)")
	public int getYear() {
		return year;
	}
	
	
//	@Refinement("_ == old(year) + i")
//	public int addYear(int i) {
//		return year + i;
//	}
	
	
//	@Refinement("_ == year")
//	public int getAge(int yearNow) {
//		return (yearNow+1) - year;
//	}
	

}
