package regen.test.project;

import repair.regen.specification.PrivateRefinement;
import repair.regen.specification.Refinement;

public class Bus extends Car{
	private int year;
	@Refinement("_ > 20")
	private int seats;

	@Override
	public void setYear(@Refinement("IntBetween(_, 1000, 2010)") int x) {
		year = x;
	}

	@Refinement(" _ > 20")
	@Override
	public int getSeats() {
		return seats;
	}

	
//	@PrivateRefinement("this.year == k")//verified in visitCtReturn after
//	@Refinement(" _ >= 50")
//	public void setSeats(int k) {
//		seats = k;
//	}


	

}
