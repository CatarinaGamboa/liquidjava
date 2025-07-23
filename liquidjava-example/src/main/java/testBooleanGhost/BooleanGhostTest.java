package testBooleanGhost;

public class BooleanGhostTest {
	public static void main(String[] args) {
		BooleanGhostClass bgc = new BooleanGhostClass();

		bgc.open(); // ccomment out for error
		bgc.execute();
		bgc.close(); // comment out for error
		bgc.terminate();
	}
}
