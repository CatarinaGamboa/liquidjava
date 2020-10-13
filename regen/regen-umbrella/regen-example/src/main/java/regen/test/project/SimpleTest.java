package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	
////Original
    	@Refinement("a > 0")
    	int a = 1;
    	
    	
    	@Refinement("b == 2 || b == 3 || b == 4")
    	int b = 2;
    	
//		@Refinement("c > 2")
//    	int c = 2; // should emit error

    	
		@Refinement("d >= 2")
    	int d = b; // should be okay
		
//Arithmetic Binary Operations
    	@Refinement("t > 0")
    	int t = a + 1;
//
//Assignment after declaration
    	@Refinement("(z > 0) && (z < 50)")
    	int z = 1;
    	@Refinement("u < 100")
    	int u = 10;
    	u = 11 + z;
    	u = z*2;
    	u = 30 + z;
//    	u = 500; //error
    	

////k--
//    	@Refinement("k > 0")
//    	int k = 1;
//    	k = k - 1;  	
    	
////Arithmetic operation with variable - ????
//    	@Refinement("a > 0")
//    	int a = 10;
//    	@Refinement("t > 10")
//    	int t = 2 + a;

    	
    	
		
    }
}
