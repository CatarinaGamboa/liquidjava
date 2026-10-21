package liquidjava.diagnostics.hints;

/**
 * A suggestion on how to fix a diagnostic
 *
 * @param kind
 *            what the suggestion is about, so clients can show or filter hints separately
 * @param message
 *            the suggestion as shown to the user
 */
public record Hint(Kind kind, String message) {

    public enum Kind {
        /** A suggestion that does not come from a specific analysis */
        GENERAL,
        /** Calls to add before the failing call so the object reaches a state that allows it */
        CALL_SEQUENCE,
        /** The calls allowed in the object's current state */
        AVAILABLE_CALLS
    }

    public static Hint general(String message) {
        return new Hint(Kind.GENERAL, message);
    }
}
