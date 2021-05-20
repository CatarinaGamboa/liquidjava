package regen.test.project;

public class TrafficLight {
	
	int r; 	
	int g; 	
	int b;
	
	public TrafficLight() {
		r = 255; g = 0; b = 0; 
	}
	
	public void transitionToAmber() {
		r = 255; g = 120; b = 0;
	}
	
	public void transitionToGreen() {
		r = 76; g = 187; b = 23; 
	}
	
	public void transitionToRed() {
		r = 230; g = 0; b = -1; 
	}

}
