package testSuite.classes.boolean_ghost_correct;

public class SimpleTest {
	public static void main(String[] args) {
		SimpleStateMachine ssm = new SimpleStateMachine();
		ssm.open();
		ssm.execute();
		ssm.close();
	}
}