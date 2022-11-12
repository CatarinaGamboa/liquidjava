package liquidjava.smt;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

import liquidjava.processor.context.Context;
import liquidjava.rj_language.Predicate;
import liquidjava.smt.solver_wrapper.SMTWrapper;
import liquidjava.smt.solver_wrapper.Status;

public class SMTEvaluator {

    public void verifySubtype(Predicate subRef, Predicate supRef, Context c)
            throws TypeCheckError, GhostFunctionError, Exception {
        // Creates a parser for our SMT-ready refinement language
        // Discharges the verification to z3

        Predicate toVerify = Predicate.createConjunction(subRef, supRef.negate());
        System.out.println("verification query: " + toVerify); // TODO remove

        try {
            SMTWrapper z3 = SMTWrapper.getZ3(c);
            Status s = z3.verifyExpression(toVerify.getExpression());
            if (s.equals(Status.SATISFIABLE)) {
                System.out.println("result of SMT: Not Ok!");
                throw new TypeCheckError(subRef + " not a subtype of " + supRef);
            }
            System.out.println("result of SMT: Ok!");

        } catch (SyntaxException e1) {
            System.out.println("Could not parse: " + toVerify);
            e1.printStackTrace();
        } catch (Z3Exception e) {
            if (e.getLocalizedMessage().substring(0, 24).equals("Wrong number of argument")
                    || e.getLocalizedMessage().substring(0, 13).equals("Sort mismatch"))
                throw new GhostFunctionError(e.getLocalizedMessage());
            else
                throw new Z3Exception(e.getLocalizedMessage());
        }

    }

}
