package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorAfterIf {
	public void have2(int a, int b) {
		@Refinement("pos > 0")
		int pos = 10;
		
		if(a > 0 && b > 0) {
			pos = a;
		}else {
			if( b > 0)
				pos =  b;
		}
		@Refinement("_ == a || _ == b")
		int r = pos;
	}
}
