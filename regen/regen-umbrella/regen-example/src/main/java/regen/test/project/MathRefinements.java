package regen.test.project;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;

@ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
	
	@Refinement("_ == 3.141592653589793")
	public double PI = 0;
	
	@Refinement("_ == 2.7182818284590452354")
	public double E = 0;
			
	@Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
	public int abs(int arg0);
	@Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
	public int abs(long arg0);
	@Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
	public int abs(float arg0);
	@Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
	public int abs(double arg0);
	
	@Refinement(" _ == a+b")
	public int addExact(int a, int b);
	@Refinement(" _ == a+b")
	public long addExact(long a, long b);
	
	@Refinement(" _ == a-b")
	public int subtractExact(int a, int b);
	@Refinement(" _ == a*b")
	public int multiplyExact(int a, int b);
	@Refinement("_ == (-a)")
	public int negateExact(int a);
	
	@Refinement("_ == (a-1)")
	public int decrementExact(int a);
	@Refinement("_ == (a-1)")
	public int decrementExact(long a);

	@Refinement("_ == (a+1)")
	public int incrementExact(int a);
	@Refinement("_ == (a+1)")
	public int incrementExact(long a);
	
	@Refinement("((a > b)-->( _ == a)) && ((a <= b)-->( _ == b))")
	public int max(int a, int b);//TODO CHANGE when Ite is done
	@Refinement("((a < b)-->( _ == a)) && ((a >= b)-->( _ == b))")
	public int min(int a, int b);//TODO CHANGE when Ite is done
	

	@Refinement(" _ > 0.0 && _ < 1.0")
	public long random(long a, long b);
	
	@Refinement("((sig > 0) --> (_ > 0)) && (!(sig > 0)-->!(_ > 0)) && ((_ == arg) || (_ == (-arg)))")
	public float copySign(float arg, float sig);

	
}
