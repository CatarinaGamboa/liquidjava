package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectAfterIfUsingScope {
	public static void main(String[] args) {
		//Example 1
		@Refinement("\\v < 100")
		int ielse = 90;
		
		@Refinement("\\v < 10")
		int then = 7;		
		if(then > 6)
			then = then-8;
		else
			ielse = 5;
		
		@Refinement("\\v == 7 || \\v == 5")
		int some = then;
		@Refinement("\\v == 5 || \\v==90")
		int thing = ielse;
		
	}

}
