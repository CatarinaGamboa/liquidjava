package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	@Refinement("{a < 0 }->{_ > 0}")
	public static int toPositive(int a) {
		return -a;
	}
	
	@Refinement("{a > 0 }->{_ < 0}")
	public static int toNegative(int a) {
		return -a;
	}
	
	public static void main(String[] args) {
//		@Refinement("_ < 10")
//		int a = 5;
//
//		if(a < 0) {
//			@Refinement("b < 0")
//			int b = a;
//		} else {
//			@Refinement("b >= 0")
//			int b = a;
//		}
//		
		//EXAMPLE 2
		@Refinement("_ < 10")
		int ex_a = 5;
		if(ex_a < 0) {
			@Refinement("_ >= 10")
			int ex_b = toPositive(ex_a)*10;
		}else {
//			if(ex_a != 0) {
//				@Refinement("_ < 0")
//				int ex_d = toNegative(ex_a);
//			}
			@Refinement("_ < ex_a")
			int ex_c = -10;
			
		}

	}

	


	//Errors to take care of
	// //value_4==innerScope && value_4 == innerScope_1
	//	@Refinement("_ < 100")
	//	int value = 90;
	//			
	//	if(value > 6) {
	//		@Refinement("_ > 10")
	//		int innerScope = 30;
	//		value = innerScope;
	//	}
	//	
	//	@Refinement("_ == 30 || _ == 90")
	//	int some2 = value;

	//SEE ERROR still error
	//		@Refinement("(_ == -5)")
	//		float prim = Math.copySign(-5, -500);
	//		@Refinement("_ == -656")
	//		float ter = Math.copySign(656, prim);

	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;


	//		@Refinement("true")
	//		int a = 10;
	//		int b = (a < 100)? three(): three()-1;
	//		@Refinement("c < 100")
	//		int c = (a < 100)? three(): a;
	//		c = (a < 100)? three()*3 : a*5;













}