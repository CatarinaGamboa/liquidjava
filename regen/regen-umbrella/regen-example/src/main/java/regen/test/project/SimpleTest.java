package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {	
	
//	@RefinementFunction("ghost int len(int, int, String)")
//	public static int seven() {
//		return 7;
//	}

	@Refinement("_ >= 0 && _ >= n")
	public static int sum(int n) {
		if(n <= 0)
			return 0;
		else {
			int t1 = sum(n-1);
			return n + t1;
		}
	}
	
	public static void main(String[] args) {
		//CHECK
//		@Refinement("i >= 10")
//		int i = sum(10);
	}




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