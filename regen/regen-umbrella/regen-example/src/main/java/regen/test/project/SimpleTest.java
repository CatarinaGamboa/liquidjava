package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

//	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
//    public static int posMult(int a, int b) {
//		@Refinement("y > 30")
//		int y = 50;
//    	return y-10;
//    }
//	
	@Refinement("{\\v == 10}")
    public static int ten() {
		return 10;
    }
	
	
	@Refinement("{true}->{\\v == b*2}")
	private static int multTwo(int b) {
		return b*2;
	}
//	
	public static void main(String[] args) {
//    	@Refinement("\\v >= 0")
//    	int p = 10;
//    	p = posMult(ten(), 4);
//    	
//		@Refinement("\\v < 6")
//		int z = 5;
//		
//		@Refinement("\\v > 6")
//		int x = multTwo(z);
//		
//		@Refinement("\\v == 20")
//		int y = multTwo(x);

	}


	//Errors to take care of
	// //value_4==innerScope && value_4 == innerScope_1
	//	@Refinement("\\v < 100")
	//	int value = 90;
	//			
	//	if(value > 6) {
	//		@Refinement("\\v > 10")
	//		int innerScope = 30;
	//		value = innerScope;
	//	}
	//	
	//	@Refinement("\\v == 30 || \\v == 90")
	//	int some2 = value;

	//SEE ERROR still error
	//		@Refinement("(\\v == -5)")
	//		float prim = Math.copySign(-5, -500);
	//		@Refinement("\\v == -656")
	//		float ter = Math.copySign(656, prim);

	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("\\v > 5")
	//		double c = b;


	//		@Refinement("true")
	//		int a = 10;
	//		int b = (a < 100)? three(): three()-1;
	//		@Refinement("c < 100")
	//		int c = (a < 100)? three(): a;
	//		c = (a < 100)? three()*3 : a*5;













}