package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    
	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }

	public static void main(String[] args) {
		
		
//		@Refinement("\\v <= 10")
//		int a = 1;  	

    	
    	@Refinement("\\v < 10")
    	int v = 3;
    	v--;
    	@Refinement("\\v >= 10")
    	int s = 100;
    	s--;
    	
		
    }

    

}
