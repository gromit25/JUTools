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
	
	/** 필수 여부 */
	boolean mandatory() default false;}
