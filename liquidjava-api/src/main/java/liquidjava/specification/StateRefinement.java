package liquidjava.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to create state transitions in a method. The annotation has two arguments: from : the
 * state in which the object needs to be for the method to be invoked correctly to : the state in
 * which the object will be after the execution of the method
 * e.g. @StateRefinement(from="open(this)", to="closed(this)")
 *
 * @author catarina gamboa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Repeatable(StateRefinementMultiple.class)
public @interface StateRefinement {
    public String from() default "";

    public String to() default "";
}
