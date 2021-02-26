package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")
public class CorrectAliasExpressions {
	
	@Refinement("InRange( _, 10, 16)")
	public static int getNum() {
		return 15;
	}
	
	public static void main(String[] args) {
		
		@Refinement("InRange( _, 10, 122+5)")
		int j = getNum();
		
	}

}
