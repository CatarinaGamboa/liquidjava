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
	
	public static void main(String[] args) {
		int[] a = new int[10];
//		getIndexWithValue(a, 0, 50);
		a[0] = 5;
		//getIndexWithValue(a, 0, 6);
	}
					
		//		//@Refinement("_.length(x) >= 0") ==
		////	@Refinement("length(_, x) >= 0")
		////	int[] a1 = new int[5]; //Cannot prove - len() built-in
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