package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * 
 * 
 * @author jmsohn
 */
class StringTypeShift implements TypeShift {

	@Override
	public void setValue(Map<String, Object> map, String name, String value) throws Exception {
		map.put(name, value);
	}
}
