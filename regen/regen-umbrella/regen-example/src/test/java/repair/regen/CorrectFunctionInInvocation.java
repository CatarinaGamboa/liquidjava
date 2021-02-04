package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionInInvocation {
	@Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
	
	@Refinement("{_ == 10}")
    public static int ten() {
		return 10;
    }
	
	
	@Refinement("{true}->{_ == b*2}")
	private static int multTwo(int b) {
		return b*2;
	}
	
	public static void main(String[] args) {
    	@Refinement("_ >= 0")
    	int p = 10;
    	p = posMult(ten(), 4);
    	
		@Refinement("_ < 6")
		int z = 5;
		
		@Refinement("_ > 6")
		int x = multTwo(z);
		
		@Refinement("_ == 20")
		int y = multTwo(x);

	}
	
	
}
