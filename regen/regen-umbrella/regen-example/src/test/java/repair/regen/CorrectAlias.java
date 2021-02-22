package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("type PtGrade(int x) { x >= 0 && x <= 20}")
public class CorrectAlias {
	
	public static void main(String[] args) {
		@Refinement("PtGrade(_) && _ >= 10")
		int positiveGrade = 15;
	}

}
