package repair.regen.classes.refs_from_superclasses_correct;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class Bus extends Car {

	private int year;
	@Refinement("_ > 20")
	private int seats;

	@Override
	public void setYear(@Refinement("IntBetween(x, 1000, 2100)") int x) {
		year = x;
	}

	@Refinement(" _ > 20 && _ < h")
	@Override
	public int getSeats(int h) {
		if( seats < h)
			return seats;
		else
			return 21;//dummy
	}

	
//	@PrivateRefinement("this.year == k")//verified in visitCtReturn after
//	@Refinement(" _ >= 50")
//	public void setSeats(int k) {
//		seats = k;
//	}
//

	

}
