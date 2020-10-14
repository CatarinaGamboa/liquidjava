package repair.regen.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.hamcrest.core.IsInstanceOf;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.Filter;
import spoon.support.reflect.code.CtVariableWriteImpl;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference
	private final String REFINE_KEY = "refinement";
	private int counter = 0;
	private Stack<Map<String, CtTypeReference<?>>> ctx = new Stack<>();

	public RefinementTypeChecker() {
		ctx.add(new HashMap<>()); // default context
	}


	private Map<String, CtTypeReference<?>> getContext() {
		Map<String, CtTypeReference<?>> f = new HashMap<>();
		for (Map<String, CtTypeReference<?>> frame : ctx) {
			for (String s : frame.keySet()) {
				f.put(s, frame.get(s));
			}
		}
		return f;
	}

	private void addToContext(String s, CtTypeReference<?> t) {
		ctx.peek().put(s,  t);
	}

	/**
	 * Visitor for binary operations
	 * Adds metadata to the binary operations from the operands
	 */
	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		super.visitCtBinaryOperator(operator);
		StringBuilder sb = new StringBuilder(); 
		CtExpression<?> right = operator.getRightHandOperand();
		CtExpression<?> left = operator.getLeftHandOperand();
		String oper = operator.toString();
		CtElement parent = operator.getParent();
		
		if(parent instanceof CtAssignment<?, ?>) {
			CtVariableWriteImpl<?> parentVar = (CtVariableWriteImpl<?>)((CtAssignment) parent)
					.getAssigned();
			oper = getOperationRefinements(operator, parentVar, operator, sb);
		}else if( parent instanceof CtLocalVariable<?>) {
			String varRight = getOperationRefinements(operator, right, sb);
			String varLeft = getOperationRefinements(operator, left, sb);
			oper =  varLeft +" "+ getOperatorFromKind(operator.getKind()) +" "+ varRight;
		}
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata(REFINE_KEY, "\\v == " + oper+ sb.toString());
		}
	}

	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);

		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		getVariableMetadada(variableRead, varDecl);
	}


	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata(REFINE_KEY, "\\v == " + lit.getValue());
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		//only declaration, no assignment
		if(localVariable.getAssignment() == null) return;

		String refinementFound = (String) localVariable.getAssignment().getMetadata(REFINE_KEY);

		if (refinementFound == null) {
			refinementFound = "True";
		}

		checkVariableRefinements(refinementFound, localVariable.getSimpleName(), localVariable);

	}

	@Override
	public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> assignement) {
		super.visitCtAssignment(assignement);	
		CtExpression<T> ex =  assignement.getAssigned();

		if (ex instanceof CtVariableWriteImpl) {
			CtVariable<T> varDecl = (CtVariable<T>) ((CtVariableAccess<?>) ex)
					.getVariable()
					.getDeclaration();
			getVariableMetadada(ex, varDecl);

			String refinementFound = (String) assignement.getAssignment().getMetadata(REFINE_KEY);
			if (refinementFound == null)
				refinementFound = "True";
			checkVariableRefinements(refinementFound, varDecl.getSimpleName(), varDecl);
		}
	}





	private <T> void checkVariableRefinements(String refinementFound, String simpleName, CtVariable<T> variable) {
		String correctRefinement = refinementFound.replace("\\v", simpleName);
		variable.putMetadata(REFINE_KEY, correctRefinement);

		Optional<String> expectedType = variable.getAnnotations().stream()
				.filter(
						ann -> ann.getActualAnnotation().annotationType().getCanonicalName()
						.contentEquals("repair.regen.specification.Refinement")
						).map(
								ann -> (CtLiteral<String>) ann.getAllValues().get("value")
								).map(
										str -> str.getValue()
										).findAny();

		expectedType.ifPresent((et) -> {
			System.out.println("SMT subtyping:" + correctRefinement + " <: " + et);
			System.out.println("-----------------------------------------------");


			addToContext(variable.getSimpleName(), variable.getType());
			try {
				new SMTEvaluator().verifySubtype(correctRefinement, et, getContext());
			} catch (TypeCheckError e) {
				printError(variable, et, correctRefinement);

			}

			variable.putMetadata(REFINE_KEY, et);
			//System.out.println(variable.getAllMetadata());
		});

	}
	
	private <T> void getVariableMetadada(CtElement variable, CtVariable<T> varDecl) {
		String refinementFound = (String) varDecl.getMetadata(REFINE_KEY);
		if (refinementFound == null) {
			refinementFound = "True";
		}
		variable.putMetadata(REFINE_KEY, "(\\v == " + 
				varDecl.getSimpleName() + 
				") && ("+ refinementFound + ")");
	}	
	
	/**
	 * Get the String value of the operator from the enum
	 * @param kind
	 * @return
	 */
	private String getOperatorFromKind(BinaryOperatorKind kind) {
		switch(kind) {
		case PLUS:	return "+";
		case MINUS: return "-";
		case MUL: 	return "*";
		case DIV: 	return "/";
		default:
			return null;
			//TODO COMPLETE
		}
	}

	private String getOperationRefinements(CtBinaryOperator<?> operator, 
			CtExpression<?> element, StringBuilder sb) {
		return getOperationRefinements(operator, null, element, sb);
	}

	/**
	 * Retrieves all the refinements for the Operation including the refinements of all operands
	 * @param operator Binary Operator that started the operation
	 * @param parentVar Parent of Binary Operator, usually a CtAssignment or CtLocalVariable
	 * @param element CtExpression that represent an Binary Operation or one of the operands
	 * @param sb StringBuilder that joins the refinements of all the elements in the operation
	 * @return
	 */
	private String getOperationRefinements(CtBinaryOperator<?> operator, CtVariableWriteImpl<?> parentVar, 
			CtExpression<?> element, StringBuilder sb) {
		if(element instanceof CtVariableRead<?>) {
			CtVariableRead<?> elemVar = (CtVariableRead<?>) element;
			String elemName = elemVar.getVariable().getSimpleName();
			String elem_ref = (String) element.getMetadata(REFINE_KEY);

			//same name as caller k = k +...
			CtElement parent = operator.getParent();
			if(parent instanceof CtAssignment) {
				CtExpression<?> parent_var = ((CtAssignment) parent).getAssigned();
				if(parent_var instanceof CtVariableWriteImpl) {
					String parentName = parentVar.getVariable().getSimpleName();
					if(parentName.equals(elemName)) {
						elemName = "VV_"+counter++;//+parentName;
						addToContext(elemName, parentVar.getType());
						elem_ref = elem_ref.replaceAll(parentName, elemName);

					}
				}
			}
			sb.append(" && "+elem_ref.replace("\\v", elemName));
			return elemName;
		}
		else if(element instanceof CtBinaryOperator<?>) {
			CtBinaryOperator<?> binop = (CtBinaryOperator<?>) element;
			String right = getOperationRefinements(operator, parentVar, binop.getRightHandOperand(), sb);
			String left = getOperationRefinements(operator, parentVar, binop.getLeftHandOperand(), sb);
			return left +" "+ getOperatorFromKind(binop.getKind()) +" "+ right;
		}else if (element instanceof CtLiteral<?>) {
			CtLiteral<?> l = (CtLiteral<?>) element;
			return l.getValue().toString();
		}
			
		//TODO Add cases
		
		return null;
	}

	/**
	 * Prints the error message
	 * @param <T>
	 * @param var
	 * @param et
	 * @param correctRefinement
	 */
	private <T> void printError(CtVariable<T> var, String et, String correctRefinement) {
		System.out.println("______________________________________________________");
		System.err.println("Failed to check refinement at: ");
		System.out.println();
		System.out.println(var);
		System.out.println();
		System.out.println("Type expected:" + et);
		System.out.println("Refinement found:" + correctRefinement);
		System.out.println("Location: " + var.getPosition());
		System.out.println("______________________________________________________");
		System.exit(1);
	}

}
