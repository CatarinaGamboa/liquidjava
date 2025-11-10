package liquidjava.smt;

import static liquidjava.diagnostics.LJDiagnostics.diagnostics;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

import liquidjava.diagnostics.errors.GhostInvocationError;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.ast.Expression;
import spoon.reflect.cu.SourcePosition;

public class SMTEvaluator {

    public void verifySubtype(Predicate subRef, Predicate supRef, Context c, SourcePosition pos)
            throws TypeCheckError, GhostFunctionError, Exception {
        // Creates a parser for our SMT-ready refinement language
        // Discharges the verification to z3

        Predicate toVerify = Predicate.createConjunction(subRef, supRef.negate());
        // System.out.println("verification query: " + toVerify); // TODO remove

        try {
            Expression exp = toVerify.getExpression();
            Status s;
            try (TranslatorToZ3 tz3 = new TranslatorToZ3(c)) {
                ExpressionToZ3Visitor visitor = new ExpressionToZ3Visitor(tz3);
                Expr<?> e = exp.accept(visitor);
                s = tz3.verifyExpression(e);

                if (s.equals(Status.SATISFIABLE)) {
                    // System.out.println("result of SMT: Not Ok!");
                    throw new TypeCheckError(subRef + " not a subtype of " + supRef);
                }
            }
            // System.out.println("result of SMT: Ok!");

        } catch (SyntaxException e1) {
            System.out.println("Could not parse: " + toVerify);
            e1.printStackTrace();
        } catch (Z3Exception e) {
            String msg = e.getLocalizedMessage().toLowerCase();
            if (msg.contains("wrong number of arguments") || msg.contains("sort mismatch"))
                diagnostics.add(new GhostInvocationError(msg, pos, supRef, null));

            throw new Z3Exception(e.getLocalizedMessage());
        }
    }
}
