package repair.regen.smt;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import repair.regen.ast.Expression;
import repair.regen.processor.constraints.Conjunction;
import repair.regen.processor.constraints.Constraint;
import repair.regen.processor.context.Context;
import repair.regen.rj_language.RefinementsParser;

public class SMTEvaluator {

	public void verifySubtype(Constraint subRef, Constraint supRef, Context c) throws TypeCheckError, GhostFunctionError, Exception {
		// TODO: create a parser for our SMT-ready refinement language
		// TODO: discharge the verification to z3 

		Constraint toVerify = Conjunction.createConjunction(subRef, supRef.negate());
		System.out.println(toVerify.toString()); //TODO remover
		
//		testAntlr(toVerify.toString(), c);
		
		try {
			Expression exp = toVerify.getExpression();
//			TranslatorToZ3 tz3 = new TranslatorToZ3(c);
//			Status s = tz3.verifyExpression(e);

			TranslatorToZ3 tz3 = new TranslatorToZ3(c);
			Expr e = exp.eval(tz3);
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
