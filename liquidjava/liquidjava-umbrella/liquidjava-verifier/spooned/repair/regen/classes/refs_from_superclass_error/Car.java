package liquidjava.classes.refs_from_superclass_error;


@liquidjava.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public class Car {
    public void setYear(@liquidjava.specification.Refinement("IntBetween(_, 1600, 2050)")
    int x) {
    }
}

