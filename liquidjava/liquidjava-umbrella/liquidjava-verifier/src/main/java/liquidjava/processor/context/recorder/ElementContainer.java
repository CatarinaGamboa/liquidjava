package liquidjava.processor.context.recorder;

public final class ElementContainer<T> extends PathElement<T> {
    private T element;

    public ElementContainer(T t) {
        this.element = t;
    }

}
