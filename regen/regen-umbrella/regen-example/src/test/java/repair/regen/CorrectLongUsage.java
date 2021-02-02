package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectLongUsage {
	
	@Refinement("{a > 10}->{ \\v > 10}")
	public static long doubleBiggerThanTen(int a){
		return a*2;
	}
	
	@Refinement("{a > 20}->{ \\v > 40}")
	public static long doubleBiggerThanTwenty(long a){
		return a*2;
	}
	
	public static void main(String[] args) {	
		@Refinement("a > 5")
		long a = 9L;
		
		if(a > 5) {
			@Refinement("b > 50")
			long b = a*10;
			
			@Refinement("c < 0")
			long c = -a;
		}
		
		@Refinement("d > 10")
		long d = doubleBiggerThanTen(100);
		
		
		@Refinement("e > 10")
		long e = doubleBiggerThanTwenty(d*2);
		
		@Refinement("\\v > 10")
		long f = doubleBiggerThanTwenty(2*80);
		
	}

}
