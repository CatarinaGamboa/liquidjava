package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectDependentRefinements {
	public static void main(String[] args) {
		
		@Refinement("_ < 10")
		int smaller = 5;
		@Refinement("bigger > 20")
		int bigger = 50;
		@Refinement("_ > smaller  && _ < bigger")
		int middle = 15;
		
		@Refinement("_ >= smaller")
		int k = 10;
		@Refinement("_ <= bigger")
		int y = 10;

		@Refinement("_ == 20")
		int x1 = 20;
		@Refinement("_ == x1 + 1")
		int x2 = 21;
		@Refinement("_ == x1 - 1")
		int x3 = 19;
		@Refinement("_ == x1 * 5")
		int x4 = x1*5;
		@Refinement("_ == x1 / 2")
		int x5 = 10;
		@Refinement("_ == x1 % 2")
		int x6 = 0;
		@Refinement("(-x7) < x1")
		int x7 = 0;
		@Refinement("_ != x1")
		int x8 = 0;
		
		@Refinement("_ == 30")
		int o = 30;
		@Refinement("_ == x1 || _ == o ")
		int x9 = 20;
	}
}
