package liquidjava.api.tests;

import org.junit.Test;

public class TestExamples extends TestAbstract {

    // @Test
    // public void interanallyInconsistentMethod() {
    // testWrong(testPath + "field_updates/InternalInconsistency.java");
    // }

    // @Test
    // public void ErrorMethodInvocation() {
    // testWrong(testPath + "ErrorMethodInvocation.java");
    // }

    // TODO: Alcides commented this because the file was missing
    // @Test
    // public void internalFieldUpdate() {
    // testCorrect(testPath + "field_updates/InternalFieldUpdate.java");
    // }

    // @Test
    // public void wrongInternalFieldUpdate() {
    // testWrong(testPath + "field_updates/WrongInternalFieldUpdate.java");
    // }

    @Test
    public void correctFieldUpdate() {
        testCorrect(testPath + "field_updates/CorrectFieldUpdate.java");
    }

    @Test
    public void incorrectFieldUpdate() {
        testWrong(testPath + "field_updates/IncorrectFieldUpdate.java");
    }

    @Test
    public void correctSimpleAssignment() {
        testCorrect(testPath + "CorrectSimpleAssignment.java");
    }

    @Test
    public void correctAssignementAfterDeclaration() {
        testCorrect(testPath + "CorrectAssignementAfterDeclaration.java");
    }

    @Test
    public void correctUnaryOperators() {
        testCorrect(testPath + "CorrectUnaryOperators.java");
    }

    @Test
    public void correctImplies() {
        testCorrect(testPath + "CorrectImplies.java");
    }

    @Test
    public void correctNoRefinements() {
        testCorrect(testPath + "CorrectNoRefinements.java");
    }

    @Test
    public void correctFunctionDeclarations() {
        testCorrect(testPath + "CorrectFunctionDeclarations.java");
    }

    @Test
    public void correctFunctionInvocation() {
        testCorrect(testPath + "CorrectFunctionInvocation.java");
    }

    @Test
    public void correctFunctionInInvocation() {
        testCorrect(testPath + "CorrectFunctionInInvocation.java");
    }

    @Test
    public void correctFunctionCallSameVariableName() {
        testCorrect(testPath + "CorrectFunctionCallSameVariableName.java");
    }

    @Test
    public void correctDependentRefinements() {
        testCorrect(testPath + "CorrectDependentRefinements.java");
    }

    @Test
    public void correctSimpleIfThen() {
        testCorrect(testPath + "CorrectIfThen.java");
    }

    @Test
    public void correctTernaryExpression() {
        testCorrect(testPath + "CorrectTernaryExpression.java");// TODO REVIEW TEST
    }

    @Test
    public void correctSimpleIfElse() {
        testCorrect(testPath + "CorrectSimpleIfElse.java");
    }

    @Test
    public void correctBooleanLitAndInvocations() {
        testCorrect(testPath + "CorrectBooleanLitAndInvocations.java");
    }

    @Test
    public void correctRecursion() {
        testCorrect(testPath + "CorrectRecursion.java");
    }

    @Test
    public void correctSearchIndexIntArray() {
        testCorrect(testPath + "CorrectSearchIndexIntArray.java");
    }

    @Test
    public void correctMethodInvocation() {
        testCorrect(testPath + "CorrectMethodInvocation.java");
    }

    @Test
    public void correctChainedVariableReferences() {
        testCorrect(testPath + "CorrectChainedVariableReferences.java");
    }

    @Test
    public void correctReadSpecificAssignment() {
        testCorrect(testPath + "CorrectReadSpecificAssignment.java");
    }

    @Test
    public void correctSpecificFunctionInvocation() {
        testCorrect(testPath + "CorrectSpecificFunctionInvocation.java");
    }

    @Test
    public void correctUsingAfterIf() {
        testCorrect(testPath + "CorrectUsingAfterIf.java");
    }

    @Test
    public void correctAfterIfUsingScope() {
        testCorrect(testPath + "CorrectAfterIfUsingScope.java");
    }

    @Test
    public void correctFunctionsTutorial() {
        testCorrect(testPath + "CorrectFunctionsTutorial.java");
    }

    @Test
    public void correctSearchValueIndexArray() {
        testCorrect(testPath + "CorrectSearchValueIndexArray.java");
    }

    @Test
    public void correctAlias() {
        testCorrect(testPath + "CorrectAlias.java");
    }

    @Test
    public void correctAliasMultiple() {
        testCorrect(testPath + "CorrectAliasMultiple.java");
    }

    @Test
    public void correctAliasExpressions() {
        testCorrect(testPath + "CorrectAliasExpressions.java");
    }

    @Test
    public void errorSimpleAssignment() {
        testWrong(testPath + "ErrorSimpleAssignment.java");
    }

    @Test
    public void errorAssignementAfterDeclaration() {
        testWrong(testPath + "ErrorAssignementAfterDeclaration.java");
    }

    @Test
    public void errorUnaryOperators() {
        testWrong(testPath + "ErrorUnaryOperators.java");
    }

    @Test
    public void errorNoRefinementsVar() {
        testWrong(testPath + "ErrorNoRefinementsInVar.java");
    }

    @Test
    public void errorFunctionDeclarations() {
        testWrong(testPath + "ErrorFunctionDeclarations.java");
    }

    @Test
    public void errorFunctionInvocation() {
        testWrong(testPath + "ErrorFunctionInvocation.java");
    }

    @Test
    public void errorFunctionInvocation1() {
        testWrong(testPath + "ErrorFunctionInvocation1.java");
    }

    @Test
    public void errorFunctionInvocationParams() {
        testWrong(testPath + "ErrorFunctionInvocationParams.java");
    }

    @Test
    public void errorDependentRefinement() {
        testWrong(testPath + "ErrorDependentRefinement.java");
    }

    @Test
    public void errorIfAssignment() {
        testWrong(testPath + "ErrorIfAssignment.java");
    }

    @Test
    public void errorIfAssignment2() {
        testWrong(testPath + "ErrorIfAssignment2.java");
    }

    @Test
    public void errorTernaryExpression() {
        testWrong(testPath + "ErrorTernaryExpression.java");
    }

    @Test
    public void errorBooleanLiteral() {
        testWrong(testPath + "ErrorBooleanLiteral.java");
    }

    @Test
    public void errorBooleanFunInvocation() {
        testWrong(testPath + "ErrorBooleanFunInvocation.java");
    }

    @Test
    public void errorIfSpecificValueAssignment() {
        testWrong(testPath + "ErrorSpecificValuesIf2.java");
    }

    @Test
    public void errorAfterIf() {
        testWrong(testPath + "ErrorAfterIf.java");
    }

    @Test
    public void errorAfterIf2() {
        testWrong(testPath + "ErrorAfterIf2.java");
    }

    @Test
    public void errorSpecificValuesIf() {
        testWrong(testPath + "ErrorSpecificValuesIf.java");
    }

    @Test
    public void errorSpecificVarInRefinement() {
        testWrong(testPath + "ErrorSpecificVarInRefinement.java");
    }

    @Test
    public void errorSpecificVarInRefinementIf() {
        testWrong(testPath + "ErrorSpecificVarInRefinementIf.java");
    }

    @Test
    public void errorRecursion1() {
        testWrong(testPath + "ErrorRecursion1.java");
    }

    @Test
    public void errorGhostArgsTypes() {
        testWrong(testPath + "ErrorGhostArgsTypes.java");
    }

    @Test
    public void errorGhostNumberArgs() {
        testWrong(testPath + "ErrorGhostNumberArgs.java");
    }

    @Test
    public void errorSearchIntArray() {
        testWrong(testPath + "ErrorSearchIntArray.java");
    }

    @Test
    public void errorLenZeroIntArray() {
        testWrong(testPath + "ErrorLenZeroIntArray.java");
    }

    @Test
    public void errorSyntax1() {
        testWrong(testPath + "ErrorSyntax1.java");
    }

    @Test
    public void errorSearchValueIntArray1() {
        testWrong(testPath + "ErrorSearchValueIntArray1.java");
    }

    @Test
    public void errorSearchValueIntArray2() {
        testWrong(testPath + "ErrorSearchValueIntArray2.java");
    }

    @Test
    public void errorImplementationSearchValueIntArray() {
        testWrong(testPath + "ErrorImplementationSearchValueIntArray.java");
    }

    @Test
    public void errorAliasSimple() {
        testWrong(testPath + "ErrorAliasSimple.java");
    }

    @Test
    public void errorAlias() {
        testWrong(testPath + "ErrorAlias.java");
    }

    @Test
    public void errorAliasTypeMismatch() {
        testWrong(testPath + "ErrorAliasTypeMismatch.java");
    }

    @Test
    public void errorAliasArgumentSize() {
        testWrong(testPath + "ErrorAliasArgumentSize.java");
    }

    @Test
    public void errorTypeInRefinements() {
        testWrong(testPath + "ErrorTypeInRefinements.java");
    }

}
