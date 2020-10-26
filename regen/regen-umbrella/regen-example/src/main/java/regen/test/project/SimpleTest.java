package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	public static void main(String[] args) {
		
		@Refinement("\\v < 10")
		int smaller = 5;
		@Refinement("bigger > 20")
		int bigger = 50;
		@Refinement("\\v > smaller  && \\v < bigger")
		int middle = 15;
		
		@Refinement("\\v >= smaller")
		int k = 10;
		@Refinement("\\v <= bigger")
		int y = 10;

		@Refinement("\\v == 20")
		int x1 = 20;
		@Refinement("\\v == x1 + 1")
		int x2 = 21;
		@Refinement("\\v == x1 - 1")
		int x3 = 19;
		@Refinement("\\v == x1 * 5")
		int x4 = x1*5;
		@Refinement("\\v == x1 / 2")
		int x5 = 10;
		@Refinement("\\v == x1 % 2")
		int x6 = 0;
		@Refinement("(-x7) < x1")
		int x7 = 0;
		@Refinement("\\v != x1")
		int x8 = 0;
		
		@Refinement("\\v == 30")
		int o = 30;
		@Refinement("\\v == x1 || \\v == o ")
		int x9 = 20;

		
//		int a = 3;
//		@Refinement("b < 10")
//		int b = a;
	}
}
