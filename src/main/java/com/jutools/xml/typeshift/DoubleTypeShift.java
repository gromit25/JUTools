package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * Double 타입 변환 클래스
 * 
 * @author jmsohn
 */
class DoubleTypeShift implements TypeShift {

	@Override
	public void setValue(Map<String, Object> map, String name, String value) throws Exception {
		map.put(name, Double.parseDouble(value));
	}
}
