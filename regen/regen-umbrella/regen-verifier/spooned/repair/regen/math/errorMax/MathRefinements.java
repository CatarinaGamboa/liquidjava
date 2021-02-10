package repair.regen.math.errorMax;


// java.lang.Math.max(int,int):@Refinement("((arg0 > arg1)-->( _ == arg0)) && ((arg0 <= arg1)-->( _ == arg1))")java.lang.Math.max(int,int)
// java.lang.Math.min(int,int):@Refinement("((arg0 < arg1)-->( _ == arg0)) && ((arg0 >= arg1)-->( _ == arg1))")java.lang.Math.min(int,int)
@repair.regen.specification.ExternalRefinementsFor("java.lang.Math")
public interface MathRefinements {
    @repair.regen.specification.Refinement("_ == 3.141592653589793")
    public double PI = 0;

    @repair.regen.specification.Refinement("_ == 2.7182818284590452354")
    public double E = 0;

    @repair.regen.specification.Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
    public int abs(int arg0);

    @repair.regen.specification.Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
    public int abs(long arg0);

    @repair.regen.specification.Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
    public int abs(float arg0);

    @repair.regen.specification.Refinement("( _ == arg0 ||  _ == -arg0) && _ > 0")
    public int abs(double arg0);

    @repair.regen.specification.Refinement(" _ == a+b")
    public int addExact(int a, int b);

    @repair.regen.specification.Refinement(" _ == a+b")
    public long addExact(long a, long b);

    @repair.regen.specification.Refinement(" _ == a-b")
    public int subtractExact(int a, int b);

    @repair.regen.specification.Refinement(" _ == a*b")
    public int multiplyExact(int a, int b);

    @repair.regen.specification.Refinement("_ == (-a)")
    public int negateExact(int a);

    @repair.regen.specification.Refinement("_ == (a-1)")
    public int decrementExact(int a);

    @repair.regen.specification.Refinement("_ == (a-1)")
    public int decrementExact(long a);

    @repair.regen.specification.Refinement("_ == (a+1)")
    public int incrementExact(int a);

    @repair.regen.specification.Refinement("_ == (a+1)")
    public int incrementExact(long a);

    @repair.regen.specification.Refinement("((a > b)-->( _ == a)) && ((a <= b)-->( _ == b))")
    public int max(int a, int b);// TODO CHANGE when Ite is done


    @repair.regen.specification.Refinement("((a < b)-->( _ == a)) && ((a >= b)-->( _ == b))")
    public int min(int a, int b);// TODO CHANGE when Ite is done


    @repair.regen.specification.Refinement(" _ > 0.0 && _ < 1.0")
    public long random(long a, long b);

    @repair.regen.specification.Refinement("((sig > 0) --> (_ > 0)) && (!(sig > 0)-->!(_ > 0)) && ((_ == arg) || (_ == (-arg)))")
    public float copySign(float arg, float sig);
}
