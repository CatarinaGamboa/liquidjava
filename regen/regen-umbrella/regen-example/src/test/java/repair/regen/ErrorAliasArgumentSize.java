package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")	
public class ErrorAliasArgumentSize {
	
	public static void main(String[] args) {
		@Refinement("InRange( _, 10)")
		int j = 15;
		
	}

}