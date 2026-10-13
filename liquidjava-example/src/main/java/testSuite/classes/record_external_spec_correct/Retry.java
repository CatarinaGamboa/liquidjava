package testSuite.classes.record_external_spec_correct;

// the methods of a record are checked against an external spec declared in another file (#342)
public record Retry(int attempts) {
    void fail(String summary) {
        Throwable exception = new Throwable(summary);
        exception.initCause(new RuntimeException("first"));
    }

    static Throwable wrap(String message, Throwable cause) {
        return new Throwable(message, cause);
    }
}
