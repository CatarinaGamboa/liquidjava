package repair.regen.api.tests;

import org.junit.Test;

public class TestState extends TestAbstract{
		
	@Test
	public void correctEmail1() {
		testCorrect(testPath+"/classes/email1");
	}

	
	@Test
	public void correctStateFromSuperclass() {
		testCorrect(testPath+"/classes/state_from_superclass_correct");
	}
	
	@Test
	public void correctStateCases() {
		testCorrect(testPath+"/classes/state_multiple_cases");
	}

	
//	@Test
//	public void correctInputReaderCharArrayLongerNameEve() {
//		testCorrect(testPath+"/classes/input_reader_correct");
//	}
//	
	
	

	@Test
	public void errorInputReader() {
		testWrong(testPath+"/classes/input_reader_error");
	}
	
	@Test
	public void errorInputReader2() {
		testWrong(testPath+"/classes/input_reader_error2");
	}
	
	@Test
	public void errorStatesSameSet() {
		testWrong(testPath+"/classes/state_multiple_error");
	}
	
	@Test
	public void errorConstructorWithFromCase() {
		testWrong(testPath+"/classes/MyStreamReader.java");
	}
	
	@Test
	public void errorEmail2() {
		testWrong(testPath+"/classes/email2");
	}
	
	@Test
	public void correctEmail3() {
		testWrong(testPath+"/classes/email3");
	}


}
