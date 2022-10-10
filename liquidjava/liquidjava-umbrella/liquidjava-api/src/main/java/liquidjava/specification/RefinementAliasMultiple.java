package liquidjava.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation to create a multiple Alias in a class
 * @author catarina gamboa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RefinementAliasMultiple {
	RefinementAlias[] value();
}
