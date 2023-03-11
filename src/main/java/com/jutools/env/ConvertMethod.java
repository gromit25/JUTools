package com.jutools.env;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConvertMethod {
	
	/** 기본형이 아닌 경우 환경변수 값을 변환할 메소드 명*/
	String method();
}
