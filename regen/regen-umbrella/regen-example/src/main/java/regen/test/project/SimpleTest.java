package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {

	
	public static void main(String[] args) {
		int a = 3;
		@Refinement("b < 10")
		int b = a;
	
//		if(a > 0) {
//			a = 12;
//		}//a > 0 && a == 12 <: a < 10
	}
}
