package regen.test.project;

import java.util.List;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
		@Refinement("\\v > 10")
		int a = 11;
		a = a * a;
//		if(a > 1) {
//			@Refinement("b > 0")
//			int b = a;
//			a = 0;
//		}
//		} else {
//			@Refinement("b <= 0")
//			int b = a;
//		}

	}
}
