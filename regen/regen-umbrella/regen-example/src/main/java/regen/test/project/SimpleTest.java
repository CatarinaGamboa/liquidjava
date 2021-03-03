package regen.test.project;


import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;

public class SimpleTest {
	
public static void main(String[] args) {
	
		Email e = new Email();
		e.from("me");
		e.to("you");
		e.subject("not important");
		e.body("body");
		e.build();
		//...
}

	
	
	
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