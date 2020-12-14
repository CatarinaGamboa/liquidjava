package regen.test.project;

import java.lang.annotation.Target;

import repair.regen.specification.Refinement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Refinement("\\v > 0")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE, 
		  ElementType.PARAMETER, ElementType.TYPE })
public @interface Positive {}




