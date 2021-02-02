package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectUsingAfterIf {
	public static void main(String[] args) {
		//Example 1
		@Refinement("\\v > 5")
		int a = 6;
		if(a > 8)
			a = 20;
		else
			a = 30;
		@Refinement("\\v == 30 || \\v == 20")
		int b = a;

		//Example 2
		@Refinement("y < 100")
		int y = 50;
		if(y > 2)
		    y = 3;
		else
		    y = 6;

		@Refinement("z < 7")
		int z = y;
		
		//Example 3
		@Refinement("\\v < 100")
		int changedInThenAndElse = 10;
		@Refinement("\\v > 6")
		int changeOnlyInThen = 7;
		if(changedInThenAndElse > 2) {
		    changedInThenAndElse = 3;
		    changeOnlyInThen = 8;
		}else {
		    changedInThenAndElse = 6;
		}
		@Refinement("\\v < 7")
		int ze1 = changedInThenAndElse;
		@Refinement("\\v < 9")
		int ze2 = changeOnlyInThen;
		
		//Example 4
		@Refinement("\\v < 100")
		int initializedInThen;
		if(true)
			initializedInThen = 7;
		@Refinement("\\v == 35")
		int hello = initializedInThen*5;
		
		//Example 5
		@Refinement("\\v < 100")
		int initializedInElse;
		int asds;		
		if(false)
			asds = 5;
		else
			initializedInElse = 8;
		@Refinement("\\v == 40")
		int world = initializedInElse*5;
		

		//Example 7
		@Refinement("k > 0 && k < 100")
		int k = 5;
		if(k > 7) {
			k = 9;
		}
		@Refinement("\\v < 10")
		int m = k;
		k = 50;
		@Refinement("\\v == 50")
		int m2 = k;

	}
}
