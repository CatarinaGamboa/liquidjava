package testSuite.classes.state_from_superclass_correct;

import liquidjava.specification.StateRefinement;

public class Bus extends Car {

  @StateRefinement(to = "close(this)")
  public Bus() {
  }

  @Override
  public void open() {
  }

  @Override
  public void close() {
  }
}
