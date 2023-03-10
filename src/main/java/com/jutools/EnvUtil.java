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
		new EnvMapper(type).set();
	}
	
	/**
	 * 환경변수 값을 반환함<br>
	 * 만일 없을 경우 defaultValue를 반환
	 * 
	 * @param env 환경변수 명
	 * @param defaultValue 환경변수가 설정되지 않았을 경우 반환할 값
	 * @return 설정된 환경변수 값
	 */
	public static String getEnv(String env, String defaultValue) throws Exception {
		
		String value = System.getenv(env);
		
		if(value == null) {
			return defaultValue;
		} else {
			return value;
		}
		
	}

}
