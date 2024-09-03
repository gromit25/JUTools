package com.jutools.xml.typeshift;

import java.util.Map;

/**
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface TypeShift {

	/**
	 * 
	 * @param map
	 * @param node
	 * @param value
	 */
	public void setValue(Map<String, Object> map, String name, String value) throws Exception;
}
