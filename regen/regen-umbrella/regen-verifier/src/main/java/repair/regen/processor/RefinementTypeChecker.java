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
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.Filter;

public class RefinementTypeChecker extends CtScanner {
	// This class should do the following:

	// 1. Keep track of the context variable types
	// 2. Do type checking and inference


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

	@Override
	public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
		super.visitCtBinaryOperator(operator);
		StringBuilder sb = new StringBuilder();
		for (CtElement ct : operator.getElements(new Filter<CtElement>() {

			public boolean matches(CtElement element) {
				if(element instanceof CtVariableRead<?>) {
					String elem_ref = (String) element.getMetadata("refinement");
					sb.append(elem_ref.replace("\\v", element.toString()));
				}
				return true;
			}
			//TODO Add other matches
		}))
			
		if (operator.getType().getQualifiedName().contentEquals("int")) {
			operator.putMetadata("refinement", "\\v == " + operator+ "&&" + sb.toString());
		}
	}


	@Override
	public <T> void visitCtVariableRead(CtVariableRead<T> variableRead) {
		super.visitCtVariableRead(variableRead);

		CtVariable<T> varDecl = variableRead.getVariable().getDeclaration();
		String refinementFound = (String) varDecl.getMetadata("refinement");
		if (refinementFound == null) {
			refinementFound = "True";
		}
		variableRead.putMetadata("refinement", "(\\v == " + 
				variableRead.getVariable().getSimpleName() + 
				") && ("+ refinementFound + ")");
		
	}


	@Override
	public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata("refinement", "\\v == " + lit.getValue());
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		//only declaration, no assignment
		if(localVariable.getAssignment() == null) return;

		String refinementFound = (String) localVariable.getAssignment().getMetadata("refinement");
		//System.out.println("****refinementFound:"+localVariable.getAssignment());
		CtExpression<T> a = localVariable.getAssignment();
		if (refinementFound == null) {
			refinementFound = "True";
		}
		String correctRefinement = refinementFound.replace("\\v", localVariable.getSimpleName());
		localVariable.putMetadata("refinement", correctRefinement);

		Optional<String> expectedType = localVariable.getAnnotations().stream().filter(
				ann -> ann.getActualAnnotation().annotationType().getCanonicalName().contentEquals("repair.regen.specification.Refinement")
				).map(
						ann -> (CtLiteral<String>) ann.getAllValues().get("value")
						).map(
								str -> str.getValue()
								).findAny();

		expectedType.ifPresent((et) -> {
			//System.out.println("SMT subtyping:" + correctRefinement + " <: " + et);


			addToContext(localVariable.getSimpleName(), localVariable.getType());
			try {
				new SMTEvaluator().verifySubtype(correctRefinement, et, getContext());
			} catch (TypeCheckError e) {
				System.out.println("______________________________________________________");
				System.err.println("Failed to check refinement at: ");
				System.out.println();
				System.out.println(localVariable);
				System.out.println();
				System.out.println("Type expected:" + et);
				System.out.println("Refinement found:" + correctRefinement);
				System.out.println("Location: " + localVariable.getPosition());
				System.out.println("______________________________________________________");
				System.exit(1);
			}
			localVariable.putMetadata("refinement", et);
			localVariable.getAssignment().putMetadata("refinement", et);
		});
	}

	@Override
	public <T,A extends T> void visitCtAssignment(CtAssignment<T,A> assignement) {
		super.visitCtAssignment(assignement);	
		//TODO	idea: int x;	x = 10;
	}


}
