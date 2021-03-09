package repair.regen.classes.refs_from_interface_error;


@repair.regen.specification.RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public interface ICar {
    public void setYear(@repair.regen.specification.Refinement("IntBetween(_, 1600, 2050)")
    int y);
}

