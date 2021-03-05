package repair.regen.api;

import static org.junit.Assert.fail;

import java.security.Permission;

import org.junit.Test;

import repair.regen.language.parser.SyntaxException;

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
	public void correctUnaryOperators() {
		testCorrect(testPath+"CorrectUnaryOperators.java");
	}
	
	
	@Test
	public void correctImplies() {
		testCorrect(testPath+"CorrectImplies.java");
	}
	
	@Test
	public void correctLongUsage() {
		testCorrect(testPath+"CorrectLongUsage.java");
	}
	
	@Test
	public void correctPrimitiveNumbersTypes() {
		testCorrect(testPath+"CorrectPrimitiveNumbersTypes.java");
	} //Takes a long time
	
	@Test
	public void correctFPArithmetic() {
		testCorrect(testPath+"CorrectFPArithmetic.java");
	}
	
	
	@Test
	public void correctNoRefinements() {
		testCorrect(testPath+"CorrectNoRefinements.java");
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
	
	@Test
	public void correctTernaryExpression() {
		testCorrect(testPath+"CorrectTernaryExpression.java");//TODO REVIEW TEST
	}
	
	@Test
	public void correctSimpleIfElse() {
		testCorrect(testPath+"CorrectSimpleIfElse.java");
	}
	
	@Test
	public void correctBooleanLitAndInvocations() {
		testCorrect(testPath+"CorrectBooleanLitAndInvocations.java");
	}
	
	@Test
	public void correctRecursion() {
		testCorrect(testPath+"CorrectRecursion.java");
	}
	
	@Test
	public void correctSearchIndexIntArray() {
		testCorrect(testPath+"CorrectSearchIndexIntArray.java");
	}
	
	@Test
	public void correctMethodInvocation() {
		testCorrect(testPath+"CorrectMethodInvocation.java");
	}
	
	
	@Test
	public void correctChainedVariableReferences() {
		testCorrect(testPath+"CorrectChainedVariableReferences.java");
	}
	
	@Test
	public void correctReadSpecificAssignment() {
		testCorrect(testPath+"CorrectReadSpecificAssignment.java");
	}
	
	@Test
	public void correctSpecificFunctionInvocation() {
		testCorrect(testPath+"CorrectSpecificFunctionInvocation.java");
	}
	
	@Test
	public void correctInvocationFromMathLibrary() {
		testCorrect(testPath+"/math/correctInvocation");
	}
	
	@Test
	public void correctUsingAfterIf() {
		testCorrect(testPath+"CorrectUsingAfterIf.java");
	}
	@Test
	public void correctAfterIfUsingScope() {
		testCorrect(testPath+"CorrectAfterIfUsingScope.java");
	}
	
	@Test
	public void correctFunctionsTutorial() {
		testCorrect(testPath+"CorrectFunctionsTutorial.java");
	}
	
	@Test
	public void correctSearchValueIndexArray() {
		testCorrect(testPath+"CorrectSearchValueIndexArray.java");
	}
	
	@Test
	public void correctAlias() {
		testCorrect(testPath+"CorrectAlias.java");
	}
	
	@Test
	public void correctAliasMultiple() {
		testCorrect(testPath+"CorrectAliasMultiple.java");
	}
	
	@Test
	public void correctAliasExpressions() {
		testCorrect(testPath+"CorrectAliasExpressions.java");
	}
	
	@Test
	public void correctSimpleCarTest() {
		testCorrect(testPath+"/classes/car1");
	}
	
	@Test
	public void correctEmail1() {
		testCorrect(testPath+"/classes/email1");
	}
	
	
//	@Test
//	public void correctInputReaderCharArrayLongerNameEve() {
//		testCorrect(testPath+"/classes/input_reader_correct");
//	}
//	
	
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
	public void errorLongUsage1() {
		testWrong(testPath+"ErrorLongUsage1.java");
	}
	
	@Test
	public void errorLongUsage2() {
		testWrong(testPath+"ErrorLongUsage2.java");
	}
	
	@Test
	public void errorArithmeticFP1() {
		testWrong(testPath+"ErrorArithmeticFP1.java");
	}
	@Test
	public void errorArithmeticFP2() {
		testWrong(testPath+"ErrorArithmeticFP2.java");
	}
	@Test
	public void errorArithmeticFP3() {
		testWrong(testPath+"ErrorArithmeticFP3.java");
	}
	@Test
	public void errorArithmeticFP4() {
		testWrong(testPath+"ErrorArithmeticFP4.java");
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
	public void errorFunctionInvocation1() {
		testWrong(testPath+"ErrorFunctionInvocation1.java");
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
	
	@Test
	public void errorTernaryExpression() {
		testWrong(testPath+"ErrorTernaryExpression.java");
	}
	
	@Test
	public void errorBooleanLiteral() {
		testWrong(testPath+"ErrorBooleanLiteral.java");
	}
	
	
	@Test
	public void errorBooleanFunInvocation() {
		testWrong(testPath+"ErrorBooleanFunInvocation.java");
	}
	
	
	@Test
	public void errorIfSpecificValueAssignment() {
		testWrong(testPath+"ErrorSpecificValuesIf2.java");
	}
	
	@Test
	public void errorAfterIf() {
		testWrong(testPath+"ErrorAfterIf.java");
	}
	
	@Test
	public void errorAfterIf2() {
		testWrong(testPath+"ErrorAfterIf2.java");
	}
	
	@Test
	public void errorSpecificValuesIf() {
		testWrong(testPath+"ErrorSpecificValuesIf.java");
	}
	
	@Test
	public void errorSpecificArithmetic() {
		testWrong(testPath+"ErrorSpecificArithmetic.java");
	}

	@Test
	public void errorSpecificVarInRefinement() {
		testWrong(testPath+"ErrorSpecificVarInRefinement.java");
	}
	@Test
	public void errorSpecificVarInRefinementIf() {
		testWrong(testPath+"ErrorSpecificVarInRefinementIf.java");
	}
	
	@Test
	public void errorMathMax() {
		testWrong(testPath+"/math/errorMax");
	}

	@Test
	public void errorMathAbs() {
		testWrong(testPath+"/math/errorAbs");
	}

	@Test
	public void errorMathMultiplyExact() {
		testWrong(testPath+"/math/errorMultiplyExact");
	}
	
	
	@Test
	public void errorRecursion1() {
		testWrong(testPath+"ErrorRecursion1.java");
	}

	@Test
	public void errorGhostArgsTypes() {
		testWrong(testPath+"ErrorGhostArgsTypes.java");
	}
	
	@Test
	public void errorGhostNumberArgs() {
		testWrong(testPath+"ErrorGhostNumberArgs.java");
	}
	
	@Test
	public void errorSearchIntArray() {
		testWrong(testPath+"ErrorSearchIntArray.java");
	}
	
	@Test
	public void errorLenZeroIntArray() {
		testWrong(testPath+"ErrorLenZeroIntArray.java");
	}
	
	@Test
	public void errorSyntax1() {
		testWrong(testPath+"ErrorSyntax1.java");
	}
	
	@Test
	public void errorSearchValueIntArray1() {
		testWrong(testPath+"ErrorSearchValueIntArray1.java");
	}
	
	@Test
	public void errorSearchValueIntArray2() {
		testWrong(testPath+"ErrorSearchValueIntArray2.java");
	}
	
	@Test
	public void errorImplementationSearchValueIntArray() {
		testWrong(testPath+"ErrorImplementationSearchValueIntArray.java");
	}
	
	@Test
	public void errorAliasSimple() {
		testWrong(testPath+"ErrorAliasSimple.java");
	}
	
	@Test
	public void  errorAlias() {
		testWrong(testPath+"ErrorAlias.java");
	}
	
	@Test
	public void errorAliasTypeMismatch() {
		testWrong(testPath+"ErrorAliasTypeMismatch.java");
	}
	
	@Test
	public void errorAliasArgumentSize() {
		testWrong(testPath+"ErrorAliasArgumentSize.java");
	}
	
	@Test
	public void errorEmail2() {
		testWrong(testPath+"/classes/email2");
	}
	
	
	@Test
	public void errorInputReader() {
		testWrong(testPath+"/classes/input_reader_error");
	}
	
	
	

	
	
	class MySecurityManager extends SecurityManager {
		//Handles exit(1) when the refinements are not respected
		  @Override public void checkExit(int status) {
			  if(status == 1 || status == 2)
				  throw new SecurityException("subtyping");
		  }

		  @Override public void checkPermission(Permission perm) {
		      // Allow other activities by default
		  }
	}

}
