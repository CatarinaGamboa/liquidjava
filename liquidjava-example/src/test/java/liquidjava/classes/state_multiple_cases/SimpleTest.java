package liquidjava.classes.state_multiple_cases;

import java.io.IOException;
import java.io.InputStreamReader;
import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class SimpleTest {

  public static void main(String[] args) throws IOException {
    InputStreamReader isr = new InputStreamReader(System.in);
    @Refinement("a > -90")
    int a = isr.read();
    isr.close();
    //		isr.close();

  }

  //	InputStreamReader isr = new InputStreamReader(System.in);
  //	isr.read();
  //	isr.read();
  //	isr.read();
  //	isr.close();
  //
  //	//...
  //	isr.read();

  //	@Refinement("_ > 0")
  //	public int fun (int[] arr) {
  //		return max(arr[0], 1);
  //	}
  //

  //		//@Refinement("_.length(x) >= 0") ==
  ////	@Refinement("length(_, x) >= 0")
  ////	int[] a1 = new int[5];
  // K(.., ..)

  //	}

  // See error NaN
  //		@Refinement("true")
  //		double b = 0/0;
  //		@Refinement("_ > 5")
  //		double c = b;

}
