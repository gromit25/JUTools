package com.jutools.bytesmap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BytesMap {
	
	int order();
	int size();
	int skip() default 0;
	String method() default "";
}
