package repair.regen.smt;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import repair.regen.language.Expression;
import repair.regen.language.parser.RefinementParser;
import repair.regen.language.parser.SyntaxException;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.context.AliasWrapper;
import repair.regen.processor.context.GhostFunction;
import spoon.reflect.reference.CtTypeReference;

public class SMTEvaluator {

	public void verifySubtype(Constraint subRef, Constraint supRef, Map<String, CtTypeReference<?>> ctx, 
			List<GhostFunction> ghosts, List<AliasWrapper> alias) throws TypeCheckError, GhostFunctionError, Exception {
		// TODO: create a parser for our SMT-ready refinement language
		// TODO: discharge the verification to z3 

		Constraint toVerify = Conjunction.createConjunction(subRef, supRef.negate());
		System.out.println(toVerify.toString()); //TODO remover
		try {
			Expression e = toVerify.getExpression();
			TranslatorToZ3 tz3 = new TranslatorToZ3(ctx, ghosts, alias);
			Status s = tz3.verifyExpression(e);
			if (s.equals(Status.SATISFIABLE)) {
				throw new TypeCheckError(subRef + " not a subtype of " + supRef);
			}

		} catch (SyntaxException e1) {
			System.out.println("Could not parse: " + toVerify);
			e1.printStackTrace();
		} catch(Z3Exception e) {
			if(e.getLocalizedMessage().substring(0, 24).equals("Wrong number of argument") ||
					e.getLocalizedMessage().substring(0, 13).equals("Sort mismatch"))
				throw new GhostFunctionError(e.getLocalizedMessage());
			else
				e.printStackTrace();
		}

	}


}
