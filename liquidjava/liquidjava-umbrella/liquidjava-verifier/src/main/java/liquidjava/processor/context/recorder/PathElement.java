package liquidjava.processor.context.recorder;

public abstract class PathElement<T> {

    private PathElement<T> next = null;

    public void append(ElementContainer<T> ec) {
        if (next == null) {
            next = ec;
        } else {
            next.append(ec);
        }
    }

    public PathElement<T> forkAt(PathElement<T> forkElement) {
        if (next == forkElement) {
            Fork<T> f = new Fork<T>(next);
            this.next = f;
            return f;
        } else {
            return this.next.forkAt(forkElement);
        }
    }
}