package com.jutools;

import com.jutools.env.Env;

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

	@Env(name = "CONFIG_CLASS", method = "transferClass")
	public static Class<?> CLASS_VALUE;
	
	@Env(name = "CONFIG_STR_LIST", separator = ",")
	public static String[] STR_LIST = {};
	
	@Env(name = "CONFIG_INT_LIST", separator = ",")
	public static int[] INT_LIST = {};
	
	@Env(name = "CONFIG_CLASSES", separator = ",", method = "transferClass")
	public static Class<?>[] CLASSES_VALUE = {};
	
	/**
	 * 
	 * 
	 * @param className
	 * @return
	 */
	public static Class<?> transferClass(String className) throws Exception {
		return Class.forName(className);
	}
}