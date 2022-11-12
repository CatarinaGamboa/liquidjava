package liquidjava.smt.solver_wrapper;

import com.martiansoftware.jsap.SyntaxException;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import liquidjava.processor.context.AliasWrapper;
import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.visitors.ExpressionVisitor;
import liquidjava.smt.NotFoundError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SMTWrapper {
    public static Status verifyExpression(Expression e, Context c) throws Exception {
        Z3Translator z3tr = new Z3Translator(c);

        e.accept(z3tr);
        Expr<?> z3Expr = z3tr.getResult();

        Solver s = z3tr.getContext().mkSolver();

        s.add((BoolExpr) z3Expr);
        com.microsoft.z3.Status st = s.check();
        if (st.equals(com.microsoft.z3.Status.SATISFIABLE)) {
            // Example of values
            // System.out.println(s.getModel());
        }
        return Status.fromZ3(st);
    }
}
