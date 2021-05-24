package sum;

public class Test2 {
	
	/**
	 * The sum of all numbers between 0 and n
	 * @param n
	 * @return a positive value that represents the sum of all numbers between 0 and n
	 */
	public static int sum(int n) {
		if(n <= 1)
			return 0;
		else {
			int t1 = sum(n-1);
			return n + t1;
		}
	}


}
