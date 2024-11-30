package testSuite.classes.refs_from_interface_error;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

@RefinementAlias("IntBetween(int val, int lo, int hi) { lo <= val && val <= hi}")
public interface ICar {
  public void setYear(@Refinement("IntBetween(_, 1600, 2050)") int y);
}
