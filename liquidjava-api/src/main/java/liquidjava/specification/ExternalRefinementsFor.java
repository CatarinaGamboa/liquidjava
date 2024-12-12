package liquidjava.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to create refinements for an external library. The annotation receives the path of the
 * library e.g. @ExternalRefinementsFor("java.lang.Math")
 *
 * @author catarina gamboa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExternalRefinementsFor {
    /**
     * The prefix of the external method
     *
     * @return
     */
    public String value();
}
