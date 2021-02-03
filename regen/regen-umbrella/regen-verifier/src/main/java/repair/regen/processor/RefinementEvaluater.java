package repair.regen.processor;

import java.util.Map;
import java.util.Optional;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Status;

import repair.regen.language.Expression;
import repair.regen.language.parser.RefinementParser;
import repair.regen.smt.TranslatorToZ3;
import repair.regen.smt.TypeCheckError;
import spoon.reflect.reference.CtTypeReference;

public class RefinementEvaluater {
	public void verifyRefinement(String ref) throws TypeCheckError {
	
		Optional<Expression> parseResult = RefinementParser.parse(ref);
		if (parseResult.isPresent()) {
			Expression e = parseResult.get();
			
			
		}
		// TODO: Unknown should emit an error
		
	}



}
