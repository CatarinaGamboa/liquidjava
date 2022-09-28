package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@SuppressWarnings("unused")
@RefinementAlias("InRange(int val, int low, int up) {low < val && val < up}")	
public class ErrorAlias {
	
	@Refinement("InRange( _, 10, 16)")
	public static int getNum() {
		return 14;
	}
	
	public static void main(String[] args) {
		@Refinement("InRange( _, 10, 15)")
		int j = getNum();
	}

}
