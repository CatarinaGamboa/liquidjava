package liquidjava.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface to allow multiple state refinements in a method.
 * A method can have a state refinement for each set of different source and destination states
 * @author catarina gamboa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface StateRefinementMultiple {
	StateRefinement[] value();

}
