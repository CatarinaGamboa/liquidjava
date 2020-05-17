package repair.regen.processor;

import java.util.Optional;

import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.visitor.CtScanner;

public class RefinementTypeChecker extends CtScanner {
	@Override
    public <T> void visitCtLiteral(CtLiteral<T> lit) {
		if (lit.getType().getQualifiedName().contentEquals("int")) {
			lit.putMetadata("refinement", "\\v == " + lit.getValue());
		}
    }

	@Override
	public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
		super.visitCtLocalVariable(localVariable);
		
		String refinement = (String) localVariable.getAssignment().getMetadata("refinement");
		String correctRefinement = refinement.replace("\\v", localVariable.getSimpleName());
		localVariable.putMetadata("refinement", correctRefinement);
		
		@SuppressWarnings("unchecked")
		Optional<String> expectedType = localVariable.getAnnotations().stream().filter(
					ann -> ann.getActualAnnotation().annotationType().getCanonicalName().contentEquals("repair.regen.specification.Refinement")
				).map(
					ann -> (CtLiteral<String>) ann.getAllValues().get("value")
				).map(
					str -> str.getValue()
				).findAny();
		
		expectedType.ifPresent((et) -> {
			localVariable.getType()
			System.out.println("SMT subtyping:" + correctRefinement + "<:" + et);
			localVariable.putMetadata("refinement", et);
		});
		
		
		
		
		
	}
	
	
}
