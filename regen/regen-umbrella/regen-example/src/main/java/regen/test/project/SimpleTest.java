package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {	
	
//	@RefinementFunction("ghost int len(int, int, String)")
//	public static int seven() {
//		return 7;
//	}
//	

	
	
	public static void main(String[] args) {
		//java.util.stream.IntStream
		//IntStream.range(5,3);
		
		@Refinement("len(a + 9, 0)")
		int a;
		
		
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