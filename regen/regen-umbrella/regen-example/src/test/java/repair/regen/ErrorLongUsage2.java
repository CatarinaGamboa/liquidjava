package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorLongUsage2 {
	
	@Refinement("{a > 20}->{ \\v > 40}")
	public static long doubleBiggerThanTwenty(long a){
		return a*2;
	}
	public static void main(String[] args) {
		@Refinement("a > 5")
		long a = 9L;

		@Refinement("c > 40")
		long c = doubleBiggerThanTwenty(a*2);

	}
}
