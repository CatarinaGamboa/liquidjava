package testSuite.classes.scoreboard_error;

public class SimpleTest {
	public static void main(String[] args) {
		Scoreboard sb = new Scoreboard();
		sb.inc();
		sb.dec();
		sb.dec(); // error, underflow
		sb.finish();
	}
}