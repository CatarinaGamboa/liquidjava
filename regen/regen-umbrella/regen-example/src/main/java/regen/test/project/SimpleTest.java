package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementFunction;

@RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")	
public class SimpleTest {
	
	@Refinement("InRange( _, 10, 16)")
	public static int getNum() {
		return 15;
	}
	
	public static void main(String[] args) {
		@Refinement("a == 10")
		int a = 10;
		@Refinement("InRange( _, a, 122)")
		int j = getNum();
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}

	////correctImplies -rever!!!
	//	@Refinement("_ > 5")
	//	int x = 10;
	//	
	//	@Refinement("(x > 50) --> (y > 50)")
	//	int y = x;


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}