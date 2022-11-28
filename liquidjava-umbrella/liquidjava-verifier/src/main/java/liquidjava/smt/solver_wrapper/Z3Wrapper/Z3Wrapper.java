package liquidjava.smt.solver_wrapper.Z3Wrapper;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.Expression;
import liquidjava.smt.solver_wrapper.SMTWrapper;
import liquidjava.smt.solver_wrapper.Status;

public class Z3Wrapper implements SMTWrapper {

    Z3Translator z3tr;

    public Z3Wrapper(Context c) {
        z3tr = new Z3Translator(c);
    }

    @Override
    public Status verifyExpression(Expression e) throws Exception {
        e.accept(z3tr);
        Expr<?> z3Expr = z3tr.getResult();

        Solver s = z3tr.getContext().mkSolver();

        s.add((BoolExpr) z3Expr);
        com.microsoft.z3.Status st = s.check();

        return Status.fromZ3(st);
    }
}
