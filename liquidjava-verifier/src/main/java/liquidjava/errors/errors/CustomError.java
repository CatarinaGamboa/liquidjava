package liquidjava.errors.errors;

public class CustomError extends LJError {

    public CustomError(String message) {
        super("Found Error", message, null);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
