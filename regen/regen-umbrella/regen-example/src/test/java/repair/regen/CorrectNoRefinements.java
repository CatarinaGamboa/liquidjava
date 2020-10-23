package repair.regen;

public class CorrectNoRefinements {
	private static int addOne(int i) {
		return i+1;
	}
	private static int one() {
		return 1;
	}
	
	public static void main(String[] args) {
		
		int a = one();
		int b = a;
		int c = a*b + 50 + addOne(5);
		b = 5;
		int s = a + 10;
		s++;
	}

}
