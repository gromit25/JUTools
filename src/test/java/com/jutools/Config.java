package com.jutools;

import com.jutools.env.ArrayType;
import com.jutools.env.ConvertMethod;
import com.jutools.env.Env;

/**
 * 환경변수 설정 테스트용 설정 클래스
 * 
 * @author jmsohn
 */
public class Config {
	
	@Env(name = "CONFIG_NAME")
	public static String NAME = "SOHN JM";
	
	@Env(name = "CONFIG_BOOLEAN")
	public static boolean BOOLEAN_VALUE = false;
	
	@Env(name = "CONFIG_INT")
	public static int INT_VALUE = 0;
	
	@Env(name = "CONFIG_FLOAT")
	public static float FLOAT_VALUE = (float)0.0;
	
	@Env(name = "CONFIG_LONG")
	public static long LONG_VALUE = 0;

	@Env(name = "CONFIG_CLASS")
	@ConvertMethod(method = "transferClass")
	public static Class<?> CLASS_VALUE;
	
	@Env(name = "CONFIG_STR_LIST")
	@ArrayType(separator = ",")
	public static String[] STR_LIST = {};
	
	@Env(name = "CONFIG_INT_LIST")
	@ArrayType(separator = ",")
	public static int[] INT_LIST = {};
	
	@Env(name = "CONFIG_CLASSES")
	@ArrayType(separator = ",")
	@ConvertMethod(method = "transferClass")
	public static Class<?>[] CLASSES_VALUE = {};
	
	/**
	 * 환경변수 변환 테스트용 메소드
	 * 
	 * @param className 환경변수에 설정된 클래스의 명
	 * @return 클래스 정보
	 */
	public static Class<?> transferClass(String className) throws Exception {
		return Class.forName(className);
	}
}