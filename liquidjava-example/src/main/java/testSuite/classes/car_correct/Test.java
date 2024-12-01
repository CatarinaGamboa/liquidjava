package testSuite.classes.car_correct;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class Test {

  @Refinement("_ > 2020")
  public static int getYear() {
    return 2024;
  }

  public static void main(String[] args) {
    int a = 1998;
    Car c = new Car();
    c.setYear(a);

    @Refinement("_ > 1800")
    int j = c.getYear();

    @Refinement("_ > 2020")
    int k = getYear();
  }
}
