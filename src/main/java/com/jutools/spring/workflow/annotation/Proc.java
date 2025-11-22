package com.jutools.spring.workflow.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 액티비티 데이터 처리 메소드 어노테이션<br>
 * 아래의 형식 중 하나여야 함<br>
 * <li>List<Message<?>> method()</li>
 * <li>Message<?> method()</li>
 * <li>List<Message<?>> method(Message<?> message)</li>
 * <li>Message<?> method(Message<?> message)</li>
 * <li>void method(Message<?> message)</li>
 * <li>void method()</li>
 * 
 * @author jmsohn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Proc {

	/**
	 * 초기화 메소드<br>
	 * Proc 어노테이션 메소드가 있는 클래스의 메소드이여야 함<br>
	 * 메소드 명이 공백 문자열(blank)일 경우 초기화 메소드가 없는 것으로 간주함<br>
	 * 메소드 형식<br>
	 * public void method()
	 */
	String init() default "";
	
	/**
	 * 후처리 메소드<br>
	 * Proc 어노테이션 메소드가 있는 클래스의 메소드이여야 함<br>
	 * 메소드 명이 공백 문자열(blank)일 경우 후처리 메소드가 없는 것으로 간주함<br>
	 * 메소드 형식<br>
	 * public void method()
	 */
	String exit() default "";
}
