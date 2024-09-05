package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * Int 타입 변환 클래스
 * 
 * @author jmsohn
 */
class IntTypeShift implements TypeShift {

	@Override
	public void setValue(Map<String, Object> map, String name, String value) throws Exception {
		map.put(name, Integer.parseInt(value));
	}
}
