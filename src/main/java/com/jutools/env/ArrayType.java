package com.jutools.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ArrayType {

	/** 목록의 분리되는 문자열 */
	String separator();
	/** 목록의 분리된 문자열에 trim 적용 여부 */
	boolean trim() default true;
	
}
