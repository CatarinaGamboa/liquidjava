package liquidjava.smt.solver_wrapper;

import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.Expression;

import liquidjava.smt.solver_wrapper.CVC5Wrapper.CVC5Wrapper;
import liquidjava.smt.solver_wrapper.Z3Wrapper.Z3Wrapper;

public interface SMTWrapper {
    Status verifyExpression(Expression e) throws Exception;

    static SMTWrapper getZ3(Context c) {
        return new Z3Wrapper(c);
    }

    static SMTWrapper getCVC5(Context c) {
        return new CVC5Wrapper(c);
    }
}
