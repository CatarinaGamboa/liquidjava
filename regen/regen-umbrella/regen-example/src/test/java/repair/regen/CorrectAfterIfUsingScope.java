package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectAfterIfUsingScope {
	public static void main(String[] args) {
		//Example 1
		@Refinement("_ < 100")
		int ielse = 90;
		
		@Refinement("_ < 10")
		int then = 7;		
		if(then > 6)
			then = then-8;
		else
			ielse = 5;
		
		@Refinement("_ == 7 || _ == 5")
		int some = then;
		@Refinement("_ == 5 || _==90")
		int thing = ielse;
		
	}

}
