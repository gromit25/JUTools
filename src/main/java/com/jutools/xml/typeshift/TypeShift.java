package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * 타입 변환 인터페이스 클래스<br>
 * XMLNode의 toMap 메소드에서 value 값을 특정 데이터 형태로 변환용 
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface TypeShift {

	/**
	 * 주어진 value 를 특정 타입으로 변환하여 map 에 name 으로 저장
	 * 
	 * @param map 저장할 map
	 * @param name 저장할 이름 
	 * @param value 변환할 value
	 */
	public void setValue(Map<String, Object> map, String name, String value) throws Exception;
}
