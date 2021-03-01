package regen.test.project;

import java.util.ArrayList;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementFunction;

@RefinementAlias("PTgrade(int x) {x >= 0 && x <= 20}")
public class SimpleTest {

	public static void main(String[] args) {
		int a = 10;
		
		a = 15;
		int b = a;
		//a
		//instances: a0, a1
		//a0 == 10
		//a1 == 15
		
		//b
		//instances: b0
		//b0 == a1
	}
	
	
	
	
	
	
	
	

	
	
//	@RefinementFunction("ghost int length(int[])")
//	@Refinement("(_ >= -1) && (_ < length(l))")
//	public static int getIndexWithValue(  @Refinement("length(l) > 0") int[] l, 
//										  @Refinement("i >= 0 && i < length(l)") int i, 
//										  int val) {
//		if(l[i] == val)
//			return i;
//		if(i >= l.length - 1)
//			return -1;
//		else
//			return getIndexWithValue(l, i+1, val);	
//	}	
	
//	int[] arr = new int[10+6];
//	getIndexWithValue(arr, 0, 1000);
	
	
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