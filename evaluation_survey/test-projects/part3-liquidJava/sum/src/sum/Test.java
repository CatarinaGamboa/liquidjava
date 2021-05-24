package sum;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;

@RefinementAlias("Nat(int x) {x >= 0}")
public class Test {
    /**
    * The sum of all numbers between 0 and n
    * @param n
    * @return a positive value that represents the sum of all numbers between 0 and n
    */
    @Refinement("Nat(_) && _ >= n")
	  public static int sum(int n) {
		  if(n <= 1)//correct: 0
		  	return 0;
		  else {
			  int t1 = sum(n-1);
			  return n + t1;
		  }
  	}
    
}