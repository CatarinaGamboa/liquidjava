package repair.regen.api;

import static org.junit.Assert.fail;

import java.security.Permission;

import org.junit.Test;

public class TestExamples {

	String testPath = "../regen-example/src/test/java/repair/regen/";

	private void testCorrect(String filename) {   
		MySecurityManager secManager = new MySecurityManager();
	    System.setSecurityManager(secManager);
	    try {
	    	CommandLineLauncher.process(filename);    
	    } catch (SecurityException e) {
	      fail();
	    }
	}
	
	private void testWrong(String filename) {    
		MySecurityManager secManager = new MySecurityManager();
	    System.setSecurityManager(secManager);
	    try {
	    	CommandLineLauncher.process(filename);    
	    } catch (SecurityException e) {
	      return;
	    }
	    fail();
	}
	
	@Test
	public void correctSimpleAssignment() {
		testCorrect(testPath+"CorrectSimpleAssignment.java");
	}
	@Test
	public void correctArithmeticBinaryOperation() {
		testCorrect(testPath+"CorrectArithmeticBinaryOperations.java");
	}

	@Test
	public void correctAssignementAfterDeclaration() {
		testCorrect(testPath+"CorrectAssignementAfterDeclaration.java");
	}
	
	@Test
	public void correctFunctionDeclarations() {
		testCorrect(testPath+"CorrectFunctionDeclarations.java");
	}
	
	@Test
	public void correctFunctionInvocation() {
		testCorrect(testPath+"CorrectFunctionInvocation.java");
	}
	
	@Test
	public void correctFunctionInInvocation() {
		testCorrect(testPath+"CorrectFunctionInInvocation.java");
	}

	@Test
	public void correctUnaryOperators() {
		testCorrect(testPath+"CorrectUnaryOperators.java");
	}
	
	@Test
	public void errorSimpleAssignment() {
		testWrong(testPath+"ErrorSimpleAssignment.java");
	}
	
	@Test
	public void errorArithmeticBinaryOperation() {
		testWrong(testPath+"ErrorArithmeticBinaryOperations.java");
	}
	
	@Test
	public void errorAssignementAfterDeclaration() {
		testWrong(testPath+"ErrorAssignementAfterDeclaration.java");
	}
	
	@Test
	public void errorFunctionDeclarations() {
		testWrong(testPath+"ErrorFunctionDeclarations.java");
	}
	

	@Test
	public void errorFunctionInvocation() {
		testWrong(testPath+"ErrorFunctionInvocation.java");
	}
	
	@Test
	public void errorFunctionInvocationParams() {
		testWrong(testPath+"ErrorFunctionInvocationParams.java");
	}
	
	@Test
	public void errorUnaryOperators() {
		testWrong(testPath+"ErrorUnaryOperators.java");
	}
	
	
	class MySecurityManager extends SecurityManager {
		//Handles exit(1) when the refinements are not respected
		  @Override public void checkExit(int status) {
			  if(status == 1)
				  throw new SecurityException();
		  }

		  @Override public void checkPermission(Permission perm) {
		      // Allow other activities by default
		  }
	}

}
