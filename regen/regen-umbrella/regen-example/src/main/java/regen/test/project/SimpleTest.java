package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		if(a > 0) {
			@Refinement("b > 0")
			int b = a;
			b++;
			if(b > 10) {
				@Refinement("\\v > 0")
				int c = a;
				@Refinement("\\v > 11")
				int d = b+1;
			}
			if(a > b) {
				@Refinement("\\v > b")
				int c = a;
			}
		}

	}
}
