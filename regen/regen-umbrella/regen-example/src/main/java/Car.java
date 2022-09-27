import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public class Car{
	
	@Refinement("IntBetween(_, 1800, 2050)")
	private int year;
	private boolean isOpen;
	

	public void setYear(@Refinement("IntBetween(_, 1900, 2010)") int year) {
		this.year = year;
	}
	
	
	@Refinement("IntBetween(_, 1800, 2050)")
	public int getYear() {
		return year;
	}

	@Refinement("_ == (this.year > c.year))")
	public boolean isOlderThan(Car c) {
		return this.year > c.year;
	}

//	@StateRefinement(to = "year == old(year)+i")
//	@Refinement("_ == old(year) + i")
//	public int addYear(int i) {
//		year = year + i;
//		return year;
//	}

	
//REFINE STATE
//
//	@StateRefinement(to = "!open(this)")
//	public Car() {
//		isOpen = false;
//	}
//	
//	@StateRefinement(from = "!open(this)", to = "open(this) != open(#old)")
//	public void openCar() {
//		isOpen = true;
//	}
//	
//	
//	@StateRefinement(from = "open(this)")
//	public void passagerEnters() {
//		//...
//	}
//	
//	int a = 10;
	//a
	//instances a1, ..., an
	//a1 == 10
	//an = 15;

}
