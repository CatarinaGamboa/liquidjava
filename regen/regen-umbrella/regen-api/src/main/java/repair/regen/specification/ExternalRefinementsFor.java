package repair.regen.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ExternalRefinementsFor {
	/**
	 * The prefix of the external method 
	 * @return
	 */
	public String value();
}
