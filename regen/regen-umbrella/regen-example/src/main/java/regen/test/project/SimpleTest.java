package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {

	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;
		@Refinement("\\v > 0")
		int b = 3;
		a = (a == 2)? 6 : 9;
		a = (b > 2)? 8 : -1;
//		if(a > 0) {
//			a = 12;
//		}//a > 0 && a == 12 <: a < 10
	}
}
