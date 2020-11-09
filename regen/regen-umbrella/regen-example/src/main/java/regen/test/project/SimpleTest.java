package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {

	@Refinement("{\\v == 3}")
	public static int three() {
		return 3;
	}
	
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;
		@Refinement("\\v > 0")
		int b = 3;
		a = (a == 2)? 6 : 9;
		a = (b > 2)? 8 : -1;
		b = (a < 100)? three(): three()-1;
		@Refinement("c < 100")
		int c = (a < 100)? three(): a;
		c = (a < 100)? three()*3 : a*5;
//		if(a > 0) {
//			a = 12;
//		}//a > 0 && a == 12 <: a < 10
	}
}
