package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {


	public static void main(String[] args) {

		//SEE ERROR still error


		//		@Refinement("(\\v == -5)")
		//		float prim = Math.copySign(-5, -500);
		//		@Refinement("\\v == -656")
		//		float ter = Math.copySign(656, prim);
		@Refinement("\\v > 4")
		int d = Math.abs(-6);
		@Refinement("\\v == -6")
		int e = -Math.abs(-d);
		@Refinement("\\v == -6")
		int f = -Math.abs(e);
		@Refinement("\\v == -6")
		int f6 = -Math.abs(f);


		//See error NaN
		//		@Refinement("true")
		//		double b = 0/0;
		//		@Refinement("\\v > 5")
		//		double c = b;


		//		@Refinement("true")
		//		int a = 10;
		//		int b = (a < 100)? three(): three()-1;
		//		@Refinement("c < 100")
		//		int c = (a < 100)? three(): a;
		//		c = (a < 100)? three()*3 : a*5;

	}













}