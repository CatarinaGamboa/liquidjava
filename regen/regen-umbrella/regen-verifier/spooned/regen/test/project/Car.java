package regen.test.project;


@repair.regen.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public abstract class Car {
    public abstract void setYear(@repair.regen.specification.Refinement("IntBetween(y, 1600, 2050)")
    int y);

    @repair.regen.specification.Refinement("_ >= 1 && _ < ceil")
    public abstract int getSeats(@repair.regen.specification.Refinement("_ == 50")
    int ceil);
}

