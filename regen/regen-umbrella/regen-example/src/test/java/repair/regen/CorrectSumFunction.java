package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSumFunction {

	@Refinement("_ >= 0 && _ >= n")
	public static int sum(int n) {
		if(n <= 0)
			return 0;
		else {
			int t1 = sum(n-1);
			return n + t1;
		}
	}
	
}
