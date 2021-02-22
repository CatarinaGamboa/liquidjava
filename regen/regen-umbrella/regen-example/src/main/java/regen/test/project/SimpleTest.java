package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
@RefinementAlias("type Positive(int x) { x > 0}")
@RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class SimpleTest {	

	public static void main(String[] args) {
		@Refinement("PtGrade(_)")
		double positiveGrade2 = 20 * 0.5 + 20*0.5;
		
		@Refinement("Positive(_)")
		double positive = positiveGrade2;
	}

	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5]; //Cannot prove - len() built-in
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