package liquidjava.smt.solver_wrapper.CVC5Wrapper;

import io.github.cvc5.*;
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
        Term cvc5Expr = cvc5tr.getResult();

        Solver s = cvc5tr.getSolver();

        // heap declaration for separation logic
        s.setLogic("QF_ALL");
        s.setOption("incremental", "false");
        s.setOption("produce-models", "true");
        s.declareSepHeap(cvc5tr.getPointerSort(), cvc5tr.getPointeeSort());
        // ------------

        s.assertFormula(cvc5Expr);
        Result st = s.checkSat();

        if (st.isSat() || st.isUnknown()) {
            // Term h = s.getValueSepHeap();
            // System.out.println("Heap:\n" + h.toString());
            try {
                String model = s.getModel(new Sort[] { cvc5tr.getPointerSort(), cvc5tr.getPointeeSort() },
                        new Term[] {});

                System.out.println("Model:\n" + model);
            } catch (Exception m) {
                System.out.println("Failed to produce model: " + m);
            }

        }

        return Status.fromCVC5(st);
    }
}
