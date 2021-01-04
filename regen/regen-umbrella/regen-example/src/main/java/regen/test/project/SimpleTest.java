package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	public static void main(String[] args) {	
		@Refinement("\\v > 10")
		int a = 15;
		if(a > 14) {
			a = 12;
			@Refinement("\\v < 11")
			int c = a;
		}

		
//		@Refinement("(z > 0) && (z < 50)")
//    	int z = 1;
//    	@Refinement("u < 100")
//    	int u = 10;
//    	u = 11 + z;
//    	u = z*2;
//    	u = 30 + z;
//    	@Refinement("\\v > 0")
//    	int n = 1;
//    	n = z + n + 1 * n;
//    	@Refinement("y > 0")
//    	int y = 15;
//    	y = y*y;
	}

}

