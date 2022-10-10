package liquidjava;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectLongUsage {
	
	@Refinement("_ > 10")
	public static long doubleBiggerThanTen(@Refinement("a > 10")int a){
		return a*2;
	}
	
	@Refinement("_ > 40")
	public static long doubleBiggerThanTwenty(@Refinement("a > 20") long a){
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
		
		@Refinement("_ > 10")
		long f = doubleBiggerThanTwenty(2*80);
		
	}

}
