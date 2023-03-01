package com.jutools;

import com.jutools.env.Env;

public class Config {
	
	@Env(name = "config.name")
	public static String NAME = "SOHN JM";
	
	@Env(name = "config.boolean")
	public static boolean BOOLEAN_VALUE = false;
	
	@Env(name = "config.int")
	public static int INT_VALUE = 0;
	
	@Env(name = "config.float")
	public static float FLOAT_VALUE = (float)0.0;
	
	@Env(name = "config.long")
	public static long LONG_VALUE = 0;

	@Env(name = "config.class", method = "transferClass")
	public static Class<?> CLASS_VALUE;
	
	@Env(name = "config.str_list", separator = ",")
	public static String[] STR_LIST = {};
	
	@Env(name = "config.int_list", separator = ",")
	public static int[] INT_LIST = {};
	
	/**
	 * 
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static Class<?> transferClass(String className) throws Exception {
		return Class.forName(className);
	}
}