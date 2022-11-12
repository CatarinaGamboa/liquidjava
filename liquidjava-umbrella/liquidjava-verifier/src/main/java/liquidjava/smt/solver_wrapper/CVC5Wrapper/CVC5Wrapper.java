package liquidjava.smt.solver_wrapper.CVC5Wrapper;

import io.github.cvc5.CVC5ApiException;
import io.github.cvc5.Result;
import io.github.cvc5.Solver;
import io.github.cvc5.Term;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.Expression;
import liquidjava.smt.solver_wrapper.SMTWrapper;
import liquidjava.smt.solver_wrapper.Status;

public class CVC5Wrapper implements SMTWrapper {
    CVC5Translator cvc5tr;

    public CVC5Wrapper(Context c) throws CVC5ApiException {
        cvc5tr = new CVC5Translator(c);
    }

    @Override
    public Status verifyExpression(Expression e) throws Exception {
        e.accept(cvc5tr);
        Term z3Expr = cvc5tr.getResult();

        Solver s = cvc5tr.getSolver();

        s.assertFormula(z3Expr);
        Result st = s.checkSat();

        return Status.fromCVC5(st);
    }
}
