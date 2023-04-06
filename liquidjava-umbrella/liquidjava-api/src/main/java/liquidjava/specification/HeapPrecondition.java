package liquidjava.specification;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add a heap refinement to method or constructor
 * e.g. @HeapRefinement("x |-> ?") Object y = x;
 * @author kirill golubev
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface HeapPrecondition {
    public String value();
}
