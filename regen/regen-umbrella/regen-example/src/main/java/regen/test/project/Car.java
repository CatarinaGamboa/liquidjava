package regen.test.project;



import repair.regen.specification.RefineState;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public class Car {
	
	@Refinement("IntBetween(_, 1800, 2050)")
	private int year;
	
	
	public void setYear(@Refinement("IntBetween(_, 1900, 2000)") int year) {
		this.year = year;
	}
	
	
	@Refinement("IntBetween(_, 1800, 2050)")
	public int getYear() {
		return year;
	}
	

	
//	@Refinement("_ == GreaterThan(year, y)")
//	public boolean isOlderThan(int y) {
//		return this.year > y;
//	}

//	@Refinement("_ == old(year) + i")
//	public int addYear(int i) {
//		year = year + i;
//		return year;
//	}

	
	
	private boolean isOpen;
	
	@RefineState(to = "!open(this)")
	public Car() {
		isOpen = false;
	}
	

	@RefineState(from = "!open(this)", to = "open(this)")
	public void openCar() {
		isOpen = true;
	}
	
	@RefineState(from = "open(this)")
	public void passagerEnters() {
		//...
	}
	
//	open(Car)

}
