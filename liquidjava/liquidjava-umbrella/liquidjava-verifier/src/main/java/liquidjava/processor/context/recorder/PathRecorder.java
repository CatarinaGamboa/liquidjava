package liquidjava.processor.context.recorder;

import java.util.Stack;

/*
 * This class is used to keep track of all variables in the current context just like a stack.
 * However, whenever there is a branch, we keep the branch as well.
 */
public class PathRecorder<T> {
    private PathElement<T> root = new Root<>();
    private PathElement<T> top = root;

    private Stack<PathElement<T>> stack = new Stack<>();

    private void append(T el) {
        ElementContainer<T> ec = new ElementContainer<>(el);
        top.append(ec);
    }

    private void enterScope() {
        stack.push(top);
    }

    private void exitScope() {
        if (stack.empty()) {
            // Do nothing
        } else {
            PathElement<T> forkElement = stack.pop();
            top = root.forkAt(forkElement);
        }
    }

}
