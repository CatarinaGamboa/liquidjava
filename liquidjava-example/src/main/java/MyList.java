import java.util.ArrayList;
import liquidjava.specification.Refinement;

public class MyList {

  int[] arr = new int[20];

  @Refinement("lengthA(_) == 0")
  public ArrayList<Integer> createList() {
    return new ArrayList<Integer>();
  }

  @Refinement("lengthA(_) == (1 + lengthA(xs))")
  public ArrayList<Integer> append(ArrayList<Integer> xs, int k) {
    return null;
  }
}
