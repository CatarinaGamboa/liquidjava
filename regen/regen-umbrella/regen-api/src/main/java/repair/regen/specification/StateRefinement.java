package repair.regen.specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Repeatable(StateRefinementMultiple.class)
public @interface StateRefinement {
	public String from() default "";
	public String to()  default "";

}
