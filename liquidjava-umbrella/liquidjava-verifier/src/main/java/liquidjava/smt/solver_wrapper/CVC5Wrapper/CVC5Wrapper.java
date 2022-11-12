package liquidjava.smt.solver_wrapper.CVC5Wrapper;

import liquidjava.processor.context.Context;
import liquidjava.rj_language.ast.Expression;
import liquidjava.smt.solver_wrapper.SMTWrapper;
import liquidjava.smt.solver_wrapper.Status;
import liquidjava.smt.solver_wrapper.Z3Wrapper.Z3Translator;

public class CVC5Wrapper implements SMTWrapper {

    public CVC5Wrapper(Context c) {
        CVC5Translator cvc5tr = new CVC5Translator(c);
    }

    @Override
    public Status verifyExpression(Expression e) throws Exception {
        return null;
    }
}
