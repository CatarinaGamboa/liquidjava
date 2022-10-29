package liquidjava.smt.solver_wrapper;

public enum Status {
    UNSATISFIABLE, UNKNOWN, SATISFIABLE;

    public static Status fromZ3(com.microsoft.z3.Status s) {
        if (s.equals(com.microsoft.z3.Status.SATISFIABLE)) {
            return SATISFIABLE;
        } else if (s.equals(com.microsoft.z3.Status.UNKNOWN)) {
            return UNKNOWN;
        } else {
            return UNSATISFIABLE;
        }
    }

    public static Status fromCVC5(io.github.cvc5.Result r) {
        if (r.isNull()) {
            throw new RuntimeException("Trying to build smt-solver status from null");
        } else if (r.isSat()) {
            return SATISFIABLE;
        } else if (r.isUnknown()) {
            return UNKNOWN;
        } else {
            return UNSATISFIABLE;
        }
    }
}
