package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;
		a = 9; 
//		a = a - 6;

	}

}












