package liquidjava.logging;

import liquidjava.processor.context.PlacementInCode;
import spoon.reflect.code.CtComment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.ParentNotInitializedException;
import spoon.reflect.path.CtPath;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LogElement {
    final private CtElement elem;

    public LogElement(CtElement elem) {
        this.elem = elem;
    }

    public <A extends Annotation> A getAnnotation(Class<A> var1) {
        return elem.getAnnotation(var1);
    }

    public <A extends Annotation> boolean hasAnnotation(Class<A> var1) {
        return elem.hasAnnotation(var1);
    }

    public List<CtAnnotation<? extends Annotation>> getAnnotations() {
        return elem.getAnnotations();
    }

    public String getDocComment() {
        return elem.getDocComment();
    }

    public String getShortRepresentation() {
        return elem.getShortRepresentation();
    }

    public SourcePosition getPosition() {
        return elem.getPosition();
    }

    public List<LogElement> getAnnotatedChildren(Class<? extends Annotation> var1) {
        return elem.getAnnotatedChildren(var1).stream().map(LogElement::new).collect(Collectors.toList());
    }

    public boolean isImplicit() {
        return elem.isImplicit();
    }

    public Set<CtTypeReference<?>> getReferencedTypes() {
        return elem.getReferencedTypes();
    }

    public <E extends CtElement> List<LogElement> getElements(Filter<E> var1) {
        return elem.getElements(var1).stream().map(LogElement::new).collect(Collectors.toList());
    }

    public LogElement getParent() throws ParentNotInitializedException {
        return new LogElement(elem.getParent());
    }

    public <P extends CtElement> LogElement getParent(Class<P> var1) throws ParentNotInitializedException {
        return new LogElement(elem.getParent(var1));
    }

    public <E extends CtElement> LogElement getParent(Filter<E> var1) throws ParentNotInitializedException {
        return new LogElement(elem.getParent(var1));
    }

    public boolean isParentInitialized() {
        return elem.isParentInitialized();
    }

    public boolean hasParent(LogElement var1) {
        return elem.hasParent(var1.elem);
    }

    public CtRole getRoleInParent() {
        return elem.getRoleInParent();
    }

    public Map<String, Object> getAllMetadata() {
        return elem.getAllMetadata();
    }

    public Set<String> getMetadataKeys() {
        return elem.getMetadataKeys();
    }

    public List<CtComment> getComments() {
        return elem.getComments();
    }

    public <T> T getValueByRole(CtRole var1) {
        return elem.getValueByRole(var1);
    }

    public CtPath getPath() {
        return elem.getPath();
    }

    public Iterator<LogElement> descendantIterator() {
        return new Iterator<LogElement>() {
            final Iterator<CtElement> elemIt = elem.descendantIterator();

            @Override
            public boolean hasNext() {
                return elemIt.hasNext();
            }

            @Override
            public LogElement next() {
                return new LogElement(elemIt.next());
            }
        };
    }

    public Iterable<LogElement> asIterable() {
        return new Iterable<LogElement>() {
            final Iterable<CtElement> elemIterable = elem.asIterable();

            @Override
            public Iterator<LogElement> iterator() {
                return new Iterator<LogElement>() {
                    final Iterator<CtElement> elemIt = elemIterable.iterator();

                    @Override
                    public boolean hasNext() {
                        return elemIt.hasNext();
                    }

                    @Override
                    public LogElement next() {
                        return new LogElement(elemIt.next());
                    }
                };
            }
        };
    }

    public LogElement strippedElement() {
        CtElement elemCopy = elem.clone();
        // cleanup annotations
        if (elem.getAnnotations().size() > 0) {
            for (CtAnnotation<? extends Annotation> a : elem.getAnnotations()) {
                elemCopy.removeAnnotation(a);
            }
        }
        // cleanup comments
        if (elem.getComments().size() > 0) {
            for (CtComment a : elem.getComments()) {
                elemCopy.removeComment(a);
            }
        }
        return new LogElement(elemCopy);
    }

    public String toString() {
        return elem.toString();
    }
}
