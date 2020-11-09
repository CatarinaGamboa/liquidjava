package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		if(a > 0) {
			@Refinement("b < 0")
			int b = a;
		} else {
			@Refinement("b <= 0")
			int b = a;
		}

	}
}
