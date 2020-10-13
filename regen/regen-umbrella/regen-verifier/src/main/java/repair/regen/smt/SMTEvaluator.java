package repair.regen.smt;

import java.util.Map;
import java.util.Optional;

import com.microsoft.z3.Status;

import repair.regen.language.Expression;
import repair.regen.language.parser.RefinementParser;
import spoon.reflect.reference.CtTypeReference;

public class SMTEvaluator {
	

	
	public void verifySubtype(String subRef, String supRef, Map<String, CtTypeReference<?>> ctx) throws TypeCheckError {
		// TODO: create a parser for our SMT-ready refinement language
		// TODO: discharge the verification to z3 
		
		String toVerify = "(" + subRef + ") && !(" + supRef + ")";
		System.out.println(toVerify); //TODO remover
		Optional<Expression> parseResult = RefinementParser.parse(toVerify);
		
		if (parseResult.isPresent()) {
		
			Expression e = parseResult.get();
			TranslatorToZ3 tz3 = new TranslatorToZ3(ctx);
			Status s = tz3.verifyExpression(e);
			if (s.equals(Status.SATISFIABLE)) {
				throw new TypeCheckError(subRef + " not a subtype of " + supRef);
			}
		
		}
		// TODO: Unknown should emit an error
		
	}


}
