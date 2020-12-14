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
		testCorrect(testPath+"CorrectSimpleAssignment.java");//passVC
	}
	@Test
	public void correctArithmeticBinaryOperation() {
		testCorrect(testPath+"CorrectArithmeticBinaryOperations.java");//passVC
	}

	@Test
	public void correctAssignementAfterDeclaration() {
		testCorrect(testPath+"CorrectAssignementAfterDeclaration.java");//passVC
	}
	

	@Test
	public void correctUnaryOperators() {
		testCorrect(testPath+"CorrectUnaryOperators.java");//passVC
	}
	
	@Test
	public void correctNoRefinements() {
		testCorrect(testPath+"CorrectNoRefinements.java");//passVC
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
	public void correctFunctionCallSameVariableName() {
		testCorrect(testPath+"CorrectFunctionCallSameVariableName.java");
	}
	
	@Test
	public void correctDependentRefinements() {
		testCorrect(testPath+"CorrectDependentRefinements.java");
	}
	
	@Test
	public void correctSimpleIfThen() {
		testCorrect(testPath+"CorrectIfThen.java");
	}
	
//	@Test
//	public void correctTernaryExpression() {
//		testCorrect(testPath+"CorrectTernaryExpression.java");
//	}
//	
	@Test
	public void correctSimpleIfElse() {
		testCorrect(testPath+"CorrectSimpleIfElse.java");
	}
	
	@Test
	public void correctBooleanLitAndInvocations() {
		testCorrect(testPath+"CorrectBooleanLitAndInvocations.java");
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
	public void errorUnaryOperators() {
		testWrong(testPath+"ErrorUnaryOperators.java");
	}
	
	@Test
	public void errorUnaryOpMinus() {
		testWrong(testPath+"ErrorUnaryOpMinus.java");
	}
	
	
	@Test
	public void errorNoRefinementsVar() {
		testWrong(testPath+"ErrorNoRefinementsInVar.java");
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
	public void errorDependentRefinement() {
		testWrong(testPath+"ErrorDependentRefinement.java");
	}

	@Test
	public void errorIfAssignment() {
		testWrong(testPath+"ErrorIfAssignment.java");
	}
	
	@Test
	public void errorIfAssignment2() {
		testWrong(testPath+"ErrorIfAssignment2.java");
	}
//	
//	@Test
//	public void errorTernaryExpression() {
//		testWrong(testPath+"ErrorTernaryExpression.java");
//	}
//	
	@Test
	public void errorBooleanLiteral() {
		testWrong(testPath+"ErrorBooleanLiteral.java");
	}
	
	@Test
	public void errorBooleanFunInvocation() {
		testWrong(testPath+"ErrorBooleanFunInvocation.java");
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
