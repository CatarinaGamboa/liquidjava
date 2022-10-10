package liquidjava.processor.context.recorder;

public final class Fork<T> extends PathElement<T> {
    private PathElement<T> oldPath;

    public Fork(PathElement<T> old) {
        this.oldPath = old;
    }

}
