package com.jutools.spring.workflow.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 액티비티 크론 메소드<br>
 * 크론 호출 메소드 형식<br>
 * public void method(long baseTime, long nextTime)
 * 
 * @author jmsohn
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Cron {
	
	/** 수행 주기 - SpEL 사룡가능, ex) "${cron.period}" */
	String period();
}
