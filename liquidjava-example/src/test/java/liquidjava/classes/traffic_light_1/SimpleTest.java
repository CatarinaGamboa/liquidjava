package liquidjava.classes.traffic_light_1;

import java.io.IOException;

public class SimpleTest {

  public static void main(String[] args) throws IOException {

    TrafficLight tl = new TrafficLight();
    tl.transitionToAmber(); // correct
    tl.transitionToRed(); // correct
    tl.transitionToFlashingAmber();

    TrafficLight tl2 = tl.getTrafficLightStartingRed();
    tl2.transitionToFlashingAmber();
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
