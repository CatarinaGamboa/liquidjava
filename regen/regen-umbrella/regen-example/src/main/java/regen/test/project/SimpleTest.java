package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {
	@Refinement("\\v == 3")
	public static int three() {
		return 3;
	}
	
	@Refinement("{true}->{ \\v == (n > 10) }")
	public static boolean greaterThanTen(int n) {
		return n > 10;
	}
	
	public static void main(String[] args) {

		@Refinement("\\v < 10")
		int a = 5;

		@Refinement("\\v == true")
		boolean k = (a < 11);
		
		@Refinement("\\v == true")
		boolean t = !(a == 12);
		
		@Refinement("\\v == false")
		boolean m = greaterThanTen(a);
		
		
		
//		if(a < 0) {
//			@Refinement("b < 0")
//			int b = a;
//		} else {
//			@Refinement("b >= 0")
//			int b = a;
//		}
		
//		@Refinement("\\v > 10")
//		int a = 11;
//		if(a > three()) {
//			a = 15;
//		}else {
//			a = -10;
//		}
//		} else {
//			@Refinement("b <= 0")
//			int b = a;
//		}

	}
}
