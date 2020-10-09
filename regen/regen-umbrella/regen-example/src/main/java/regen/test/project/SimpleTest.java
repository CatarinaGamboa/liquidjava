package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	
    	@Refinement("a > 0")
    	int a = 10;
    	
//    	@Refinement("t > 0")
//    	int t = a + 1; //Missing info on a (a>0 and a == 10) -> Rule
    	
//    	@Refinement("t < 100")
//    	int u;
//    	u = 10;
    	
    	
//    	@Refinement("b == 2 || b == 3 || b == 4")
//    	int b = 2;
//    	
//		@Refinement("c > 2")
//    	int c = 2; // should emit error

    	
//		@Refinement("d >= 2")
//    	int d = b; // should be okay
//		
		
		
    }
}
