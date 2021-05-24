package fibonacci;

public class Test2 {

	 /**
	    * Computes the fibonacci of index n
	    * @param n The index of the required fibonnaci number
	    * @return The fibonacci nth number. The fibonacci sequence follows the formula Fn = Fn-1 + Fn-2 and has the starting values of F0 = 1 and F1 = 1 
	    */
	    public static int fibonacci(int n){
	        if(n <= 1)
	            return n;
	        else
	            return n * fibonacci(n-1);
	    }
}
