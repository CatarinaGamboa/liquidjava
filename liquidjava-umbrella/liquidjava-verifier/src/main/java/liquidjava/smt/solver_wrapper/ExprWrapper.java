package liquidjava.smt.solver_wrapper;

import com.microsoft.z3.Expr;
import io.github.cvc5.Term;

public class ExprWrapper {
    private final Object expr;

    private ExprWrapper(Object expr) {
        this.expr = expr;
    }

    static ExprWrapper fromZ3(Expr<?> expr) {
        return new ExprWrapper(expr);
    }

    static ExprWrapper fromCVC5(Term t) {
        return new ExprWrapper(t);
    }

    Term toCVC5() {
        if (expr instanceof Term) {
            return (Term) expr;
        }
        throw new RuntimeException(
                "Only one SMT solver is supported at time. This term \"" + expr.toString() + "\" is not from CVC5");
    }

    Expr<?> toZ3() {
        if (expr instanceof Expr) {
            // System.out.println("Casting object \"" + expr.toString() + "\" to Expr after instance check");
            Expr<?> res = (Expr<?>) expr;
            // System.out.println("Cast successful");
            return res;
        }
        throw new RuntimeException(
                "Only one SMT solver is supported at time. This term \"" + expr.toString() + "\" is not from Z3");
    }

    public String to_String() {
        return expr.toString();
    }
}
