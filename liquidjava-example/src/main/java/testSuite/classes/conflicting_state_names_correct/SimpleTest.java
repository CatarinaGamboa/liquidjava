package testSuite.classes.conflicting_state_names_correct;

class SimpleTest {
	public static void main(String[] args) {
		// both classes contain the same state names
		SM1 sm1 = new SM1();
		SM2 sm2 = new SM2();
		sm1.initialize();
		sm2.initialize();
	}
}