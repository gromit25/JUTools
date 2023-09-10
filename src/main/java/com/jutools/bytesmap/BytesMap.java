package com.jutools.bytesmap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 바이트 배열을 객체의 속성에 맵핑하기 위한 어노테이션<br>
 * order=0, size=8
 * 
 * @author jmsohn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BytesMap {
	
	/** 바이트 배열에서 속성 위치 순서(필수) */
	int order();
	
	/** 속성 크기(선택, 단 필드가 java primitive type 이어야 함) */
	int size() default -1;
	
	/** 스킵할 바이트 수(선택, 디폴트 0) */
	int skip() default 0;
	
	/**
	 * 바이트 배열을 변환할 메소드 명<br>
	 * 미설정시 문자열로 간주함, 이 경우에는 size를 필수로 설정해야 함<br>
	 * toBoolean, toShort, toInteger, toLong, toFloat, toDouble를 지원<br>
	 * 이 메소드들은 byte 배열을 문자열로 읽은 다음 해당 형태로 변경함 
	 */
	String method() default "";
}
