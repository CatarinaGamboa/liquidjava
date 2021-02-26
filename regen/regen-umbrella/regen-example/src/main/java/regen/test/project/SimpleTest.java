package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementFunction;

public class SimpleTest {
	

	@Refinement("_ >= 0 && _ >= n")
	public static int sum(int n) {
		if(n <= 0)
			return 0;
		else {
			int t1 = sum(n-1);
			return n + t1;
		}
	}
	
	@Refinement("_ >= 0 && _ >= n")
	public static int absolute(int n) {
		if(0 <= n)
			return n;
		else
			return 0 - n;
	}
	
	//From LiquidHaskell tutorial
	@Refinement("length(_) == length(vec1)")
	static int[] sumVectors(int[] vec1, @Refinement("length(vec1) == length(vec2)") int[] vec2) {
		int[] add = new int[vec1.length];
		auxSum(add, vec1, vec2, 0);
		return add;
	}

	private static void auxSum(int[] add, int[] vec1, 
							@Refinement("length(vec1) == length(vec2) && length(_) == length(add)")
							int[] vec2, 
							@Refinement("_ >= 0 && _ < length(vec2)")
							int i) {
		add[i] = vec1[i]+vec2[i];
		if(i < add.length - 1)
			auxSum(add, vec1, vec2, i+1);
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