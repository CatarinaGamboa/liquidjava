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
		int world = initializedInThen*5;
		
		//Example 6
		@Refinement("\\v < 100")
		int changedInElse = 90;
		@Refinement("\\v < 10")
		int changedInThen = 7;		
		if(changedInThen > 6)
			changedInThen = changedInThen-8;
		else
			changedInElse = 5;
		

	}
}
