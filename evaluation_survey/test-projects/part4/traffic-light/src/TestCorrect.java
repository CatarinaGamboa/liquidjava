
public class TestCorrect {
	
	public static void main(String[] args) {
		TrafficLight tl = new TrafficLight();
		tl.transitionToAmber();
		tl.transitionToRed();
		tl.transitionToGreen();
		tl.transitionToAmber();
	}

}
