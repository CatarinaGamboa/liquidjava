package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {	

//	@RefinementFunction("ghost int length(int[])")
//	public void something() {}
//	
	

	public static void searchIndex(int[] l, @Refinement("i >= 0") int i) {
		if(i >= l.length)
			return;
		else {
			@Refinement(" _ <= length(l)")
			int p = i+1;
			searchIndex(l, p);		
		}
	}
	


	public static void main(String[] args) {
		@Refinement("length(a) == 15")
		int[] a = new int[15];//Remove comments predicate

		
		searchIndex(a, 0);
		
////		@Refinement("_ >= 0 && _ < length(a)")
////		int index = 14;
////	
//		
//		
//		//@Refinement("_.length(x) >= 0") ==
////		@Refinement("length(_, x) >= 0")
////		int[] a1 = new int[5]; //Cannot prove - len() built-in
		
	}




	
	
	
	
	
	
	

	//CHECK
	//	@Refinement("i >= 0")
	//	int i = sum(10);

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