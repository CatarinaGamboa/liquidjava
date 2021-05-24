
public class TestWrong {
	
	public static void main(String[] args) {
		TrafficLight tl = new TrafficLight();
		tl.transitionToAmber();
		tl.transitionToRed();
		tl.transitionToAmber();
		tl.transitionToGreen();
	}

}
