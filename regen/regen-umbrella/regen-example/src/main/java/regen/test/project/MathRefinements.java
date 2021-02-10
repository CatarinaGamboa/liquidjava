package regen.test.project;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;

@ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
	
	@Refinement("_ == 3.141592653589793")
	public double PI = 0;
	
	@Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
	public int abs(int arg0);

}
