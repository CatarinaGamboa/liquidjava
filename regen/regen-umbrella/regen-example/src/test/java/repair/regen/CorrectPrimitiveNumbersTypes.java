package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectPrimitiveNumbersTypes {
	public static void main(String[] args) {
		@Refinement("_ > 5")
		int a = 10;
		
		@Refinement("_ > 5")
		long b = 100;
		
		@Refinement("_ > 5")
		short c = 10;
		
		@Refinement("_ > 5")
		float d = 7.4f;
		
		
		@Refinement("_ > 0")
		double e = fromType(a);
		e = fromType(b);
		e = fromType(c);
		
		@Refinement("f > 0")
		float f = twice(c);
		
	}
	
	@Refinement("{_ > 0}->{_ < i && _ > 0}")
	private static double fromType(int i) {
		return i*0.1;
	}
	
	@Refinement("{_ > 0}->{_ < i && _ > 0}")
	private static double fromType(long i) {
		return i*0.1;
	}
	
	@Refinement("{_ > 0}->{_ < i && _ > 0}")
	private static double fromType(short i) {
		return i*0.1;
	}
	
	@Refinement("{i > 0}->{_ > i}")
	private static float twice(short i) {
		return i*2f;
	}
}
