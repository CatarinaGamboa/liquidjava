package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {	
	
//	@RefinementFunction("ghost int length(int[])")
//	@Refinement("(_ >= -1) && (_ < length(l))")
//	public static int getIndexWithValue(  @Refinement("length(l) > 0") int[] l, 
//										  @Refinement("i >= 0 && i < length(l)") int i, 
//										  int val) {
//		int result;
//		if(l[i] == val)
//			result = i;
//		else if(i >= l.length-1)
//			result = -1;
//		else
//			result = getIndexWithValue(l, i+1, val);
//		return result;
//	}
	
	@Refinement("(_ == -1) || (_ == a*b)")
	public int getPositiveMult(int a, int b) {
		int result;
		if(a > 0 && b > 0)
			result = a*b;
		else
			result = -1;
		return result;
	}

	
	
	public static void main(String[] args) {

		
		
		
//		int[] a = new int[10];
//		getIndexWithValue(a, 0, max);
		
		//getIndexWithValue(a, a.length, max);
		
		//a = new int[0];
		//getIndexWithValue(a, 0, 6);
		

				
		//		//@Refinement("_.length(x) >= 0") ==
		////	@Refinement("length(_, x) >= 0")
		////	int[] a1 = new int[5]; //Cannot prove - len() built-in
		//K(.., ..)

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