package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * 문자열 변환 클래스<br>
 * 문자열은 따로 변환할 필요 없이 주어진 이름에 저장
 * 
 * @author jmsohn
 */
class StringTypeShift implements TypeShift {

	@Override
	public void setValue(Map<String, Object> map, String name, String value) throws Exception {
		map.put(name, value);
	}
}
