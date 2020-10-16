package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    
	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
//	@Refinement("{\\v > 10}")
//    public static int positive() {
//    	return 100;
//    }
	
	@Refinement("{d >= 0}->{i > d}->{\\v >= d && \\v <= d}")
	private static int range(int d, int i) {
		return d;
	}
	
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	
////Original
//    	@Refinement("a > 0")
//    	int a = 1;
//    	
//    	
//    	@Refinement("b == 2 || b == 3 || b == 4")
//    	int b = 2;
//    	
////		@Refinement("c > 2")
////    	int c = 2; // should emit error
//
//    	
//		@Refinement("d >= 2")
//    	int d = b; // should be okay
//		
////Arithmetic Binary Operations
//    	@Refinement("t > 0")
//    	int t = a + 1;
////
////Assignment after declaration
    	@Refinement("(z > 0) && (z < 50)")
    	int z = 1;
//    	@Refinement("u < 100")
//    	int u = 10;
//    	u = 11 + z;
//    	u = z*2;
//    	u = 30 + z;
//    	u = 500; //error
    	
    	

//k--
    	@Refinement("k > 0")
    	int k = 1;
    	k = z + k + 1 * k;
    	
    	@Refinement("\\v >= 0")
    	int p = 10;
    	p = posMult(10, 15-6);
    	
    	@Refinement("width > 0")
    	int width = 10;
    	@Refinement("lesser == 0")
    	int lesser = 0;

//    	int m = width-lesser;
//    	range(0, 20);
    	
//Arithmetic operation with variable - ????
//    	@Refinement("y > 0")
//    	int y = 15;
//    	y = y*y;
//    	@Refinement("t > 10")
//    	int t = 2 + a;   	
    	
		
    }

    

}
