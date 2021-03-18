package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionsTutorial {

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
		if(vec1.length > 0)
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
	
	
}
