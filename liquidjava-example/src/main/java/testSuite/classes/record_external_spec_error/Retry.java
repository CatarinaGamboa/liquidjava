package testSuite.classes.record_external_spec_error;

// the methods of a record are checked against an external spec declared in another file (#342)
public record Retry(int attempts) {
    void fail(String summary) {
        Throwable exception = new Throwable(summary, new RuntimeException("first"));
        exception.initCause(new RuntimeException("second")); // Expect: State Refinement Error
    }

    static Throwable wrap(String message) {
        Throwable t = new Throwable(message);
        t.initCause(new RuntimeException("cause"));
        return t.initCause(new RuntimeException("again")); // Expect: State Refinement Error
    }
}
