package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;

public class SimpleTest {
	@Refinement("_ < 10")
	public static int getYear() {
		return 8;
	}

	public static void main(String[] args) {
		int a = 1998;
		Car c = new Car();
		c.setYear(a);

		@Refinement("_ < 11")
		int j = getYear();
		
	}




	//	@Refinement("_ > 0")
	//	public int fun (int[] arr) {
	//		return max(arr[0], 1);
	//	}
	//	



	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}