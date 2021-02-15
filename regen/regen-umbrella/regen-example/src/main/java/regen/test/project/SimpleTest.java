package regen.test.project;

import java.io.InputStreamReader;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {	
	
	

//	@Refinement("_ >= 0 && _ >= n")
//	public static int absolute(int n) {
//		if(0 <= n)
//			return n;
//		else
//			return 0 - n;
//		
//	}
	
	@RefinementFunction("ghost boolean open(int)")
	//@Refinement("open(1) == True")
	public int add() {
		return 1;
	}
	
	public static void main(String[] args) {
		@Refinement("a > 5")
		int a = 10;
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