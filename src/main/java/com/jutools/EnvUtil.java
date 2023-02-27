package com.jutools;

import com.jutools.env.EnvMapper;

/**
 * 
 * 
 * @author jmsohn
 */
public class EnvUtil {

	/**
	 * 
	 * 
	 * @param type
	 */
	public static void set(Class<?> type) throws Exception {
		EnvMapper.set(type);
	}

}
