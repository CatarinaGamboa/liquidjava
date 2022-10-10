package liquidjava.classes.refs_from_superclasses_correct;


@liquidjava.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public abstract class Car {
    public abstract void setYear(@liquidjava.specification.Refinement("IntBetween(y, 1600, 2050)")
    int y);

    @liquidjava.specification.Refinement("_ >= 1 && _ < ceil")
    public abstract int getSeats(@liquidjava.specification.Refinement("_ == 50")
    int ceil);
}

