package com.jutools.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 필드에 환경변수 설정을 위한 어노테이션 
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Env {
	
	/** 환경변수 명 */
	String name();
	/** 목록일 경우 분리되는 문자열 */
	String separator() default "";
	/** 목록일 경우 분리된 문자열에 trim 적용 여부 */
	boolean trim() default true;
	/** 기본형이 아닌 경우 환경변수 값을 변환할 메소드 명*/
	String method() default "";
	/** 필수 여부 */
	boolean mandatory() default false;
}
