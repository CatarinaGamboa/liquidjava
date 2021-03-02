package regen.test.project;


import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;

public class SimpleTest {
	
//	public void have2(int a, int b) {
//		@Refinement("pos > 0")
//		int pos = 10;
//		if(a > 0) {
//			if(a > b)
//				pos = a-b;
//		}
//	}
//	
	public void have1(int a) {
		@Refinement("pos > 0")
		int pos = 10;
		if(a > 0) {
			pos = 5;
			pos = 8;
			pos = 30;
		}
		@Refinement("_ == 30 || _ == 10")
		int u = pos;
	}
	
	public static void main(String[] args) {
//		@Refinement("_ < 10")
//		int a = 5;
//
//		if(a > 0) {
//			@Refinement("b > 0")
//			int b = a;
//			b++;
//			if(b > 10) {
//				@Refinement("_ > 0")
//				int c = a;
//				@Refinement("_ > 11")
//				int d = b+1;
//			}
//			if(a > b) {
//				@Refinement("_ > b")
//				int c = a;
//			}
//		}
		

	}

	
//		Email e = new Email();
//		e.from("me");
//		e.to("you");
		//...


	
	
	
//	@Refinement("_ > 0")
//	public int fun (int[] arr) {
//		return max(arr[0], 1);
//	}
//	

	
	
	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}