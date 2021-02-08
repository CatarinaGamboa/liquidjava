package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {	
	
	@Refinement("_ >= a")
    public static int posMult(@Refinement("a == 10") int a, 
    						  @Refinement(" _ < a && _ > 0") int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
	
	@Refinement("_ == 10")
    public static int ten() {
		return 10;
    }
	
	
	@Refinement("_ == b*2")
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