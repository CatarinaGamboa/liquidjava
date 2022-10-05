package regen.test.project;


@liquidjava.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public interface ICar {
    public void setYear(@liquidjava.specification.Refinement("IntBetween(_, 1600, 2050)")
    int y);
}

