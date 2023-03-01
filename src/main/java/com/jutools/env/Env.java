package com.jutools.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Env {
	
	String name();
	String separator() default "";
	boolean trim() default true;
	String method() default "";
	boolean mandatory() default false;
}
