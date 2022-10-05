package liquidjava.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation to create a ghost variable for a class.
 * The annotation receives the type and name of the ghost within a string 
 * e.g. @RefinementAlias("Nat(int x) {x > 0}")
 * @author catarina gamboa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(RefinementAliasMultiple.class)
public @interface RefinementAlias {
	public String value();
}


