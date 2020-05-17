package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	int a = 1;
    	
		@Refinement("b > 3")
    	int b = 2;
    }
}
