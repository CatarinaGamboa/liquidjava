package testSuite.field_updates;

import liquidjava.specification.StateRefinement;

public class CorrectFieldUpdate {
  public int n = 0;

  @StateRefinement(from = "n(this) > 0", to = "n(this) == n(old(this))")
  public void shouldSucceedIfFieldIsPositive() {}

  public static void main(String[] args) {

    CorrectFieldUpdate t = new CorrectFieldUpdate();
    t.n = 1;
    t.shouldSucceedIfFieldIsPositive();
  }
}
