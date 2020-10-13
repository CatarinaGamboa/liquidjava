package repair.regen.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtWhile;
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
		for (CtElement ct : operator.getElements(new Filter<CtElement>() {
			public boolean matches(CtElement element) {
				//Variable Read
				if(element instanceof CtVariableRead<?>) {
					String elem_ref = (String) element.getMetadata(REFINE_KEY);
					sb.append(" && "+elem_ref.replace("\\v", element.toString()));
				}
				return true;
			}
			//TODO Possibly add other matches
		}))
			
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata(REFINE_KEY, "\\v == " + operator+ sb.toString());
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
			
//			for (CtElement ct : assignement.getAssignment().getElements(new Filter<CtElement>() {
//				public boolean matches(CtElement element) {
//					if(element instanceof CtOpera) {
//						System.out.println("SYSO:"+((CtVariable) element).getSimpleName());
//					}
//					return true;
//					if(element instanceof CtVariable<?>) {
//						System.out.println("SYSO:"+((CtVariable) element).getSimpleName());
//					}
//					return true;
//				}
//				
//			}));

			
			String refinementFound = (String) assignement.getAssignment().getMetadata(REFINE_KEY);
			if (refinementFound == null)
				refinementFound = "True";
			//System.out.println("refinementFound:"+refinementFound);

			checkVariableRefinements(refinementFound, varDecl.getSimpleName(), varDecl);
		}
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
