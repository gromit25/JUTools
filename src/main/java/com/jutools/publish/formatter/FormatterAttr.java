package com.jutools.publish.formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Formatter 속성 필드 클래스<br>
 * xml의 formatter tag의 속성과 formatter 클래스의 필드 속성을 매칭함
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormatterAttr {
	
	/** xml의 formatter tag의 속정 */
	public String name();
	/** 필수 여부 */
	public boolean mandatory() default true;

}
