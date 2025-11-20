package liquidjava.smt.errors;

public class NotFoundError extends SMTError {

    private final String name;
    private final String kind;

    public NotFoundError(String kind, String name) {
        super(String.format("%s '%s' not found", kind, name));
        this.name = name;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }
}
