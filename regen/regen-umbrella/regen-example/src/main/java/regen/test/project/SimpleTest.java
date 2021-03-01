package regen.test.project;


import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementFunction;

@RefinementAlias("PtGrade(int x) {x >= 0 && x <= 20}")
public class SimpleTest {

	@Refinement("(a > b)? (_ == a):( _ == b)")
	public static int max(@Refinement("true")int a, int b) {
		if(a > b)
			return a;
		else
			return b;
	}
	
	
//	@Refinement("_ > 0")
//	public int fun (int[] arr) {
//		return max(arr[0], 1);
//	}
//	
	
	
	
//	@Refinement("_ > 0 &&  (_ == b - a)")
//	public int range(@Refinement("a > 0")int a, @Refinement("b > a")int b) {
//		return b-a;
//	}
	
	@RefinementFunction("int lessThan(int a , int[] arr)")
	public static int getIndexWithValue(@Refinement("length(arr) > 0")int[] arr, 
										@Refinement("i >= 0 && i < length(arr)")int i, int val) {
		if(arr[i] == val)
			return i;
		if(i > arr.length - 1)
			return -1;
		else
			return getIndexWithValue(arr, i+1, val);
	}
	

	
	
	

	public static void main(String[] args) {
		
		@Refinement("length(arr) > 0")
		int[] arr = new int[10];
		

//		@Refinement("PtGrade(_) && positive >= 10")
//		int positive = 15;
//		positive = 9;
		
//		@Refinement("PtGrade(_) && _ < 10") 
//		int negative = 5;
//		positive = max(positive, negative);
//		
//		int m2 = Math.max(positive, negative); 
		

		
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