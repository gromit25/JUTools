package com.jutools.workflow.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 크론 잡 초기화 메소드 어노테이션
 * 크론 초기화 호출 메소드 형식<br>
 * public void method(long nextTime)
 * 
 * @author jmsohn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CronInit {
	
	/** 메소드 명 - SpEL 사용 불가 */
	String method();
}
