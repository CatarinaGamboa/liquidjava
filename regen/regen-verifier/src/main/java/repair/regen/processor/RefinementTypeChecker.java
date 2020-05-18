package repair.regen.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import repair.regen.smt.SMTEvaluator;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;

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
    public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata("refinement", "\\v == " + lit.getValue());
		}
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		
		String refinement = (String) localVariable.getAssignment().getMetadata("refinement");
		String correctRefinement = refinement.replace("\\v", localVariable.getSimpleName());
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
				System.out.println("Failed to check refinement at: ");
				System.out.println();
				System.out.println(localVariable);
				System.out.println();
				System.out.println("Type expected:" + et);
				System.out.println("Refinement found:" + correctRefinement);
				System.out.println("Location: " + localVariable.getPosition());
				System.out.println("______________________________________________________");
			}
			localVariable.putMetadata("refinement", et);
		});
		
		
		
		
		
	}
	
	
}
