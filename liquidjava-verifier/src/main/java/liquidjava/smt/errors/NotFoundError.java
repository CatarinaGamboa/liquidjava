package liquidjava.smt.errors;

public class NotFoundError extends SMTError {

    private final String name;

    public NotFoundError(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
