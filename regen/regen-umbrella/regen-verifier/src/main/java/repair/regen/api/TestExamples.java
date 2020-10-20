package repair.regen.api;

public class TestExamples {
	public static void main(String[] args) {
		
		String a = "../regen-example/src/test/java/repair/regen/SimpleTest.java";
		CommandLineLauncher.process(a);
	}

//	try {
//		Main.testSemantic(filename);
//	} catch (Exception e) {
//		fail(e.getMessage());
//		e.printStackTrace();
//	} 

}
