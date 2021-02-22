package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("type PtGrade(double x) { x >= 0 && x <= 20}")
public class ErrorAliasSimple {
	
	public static void main(String[] args) {
		@Refinement("PtGrade(_)")
		double positiveGrade2 = 20 * 0.5 + 20*0.6;
	}
}



