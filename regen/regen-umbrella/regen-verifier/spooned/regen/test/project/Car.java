package regen.test.project;


@repair.regen.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public abstract class Car {
    public abstract void setYear(@repair.regen.specification.Refinement("IntBetween(_, 1600, 2050)")
    int x);

    @repair.regen.specification.Refinement("_ >= 1")
    public abstract int getSeats();
}

