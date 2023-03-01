package com.jutools;

import com.jutools.env.Env;

public class Config {
	@Env(name = "config.name")
	public static String NAME;
	
	@Env(name = "config.name_list", separator = ",")
	public static String[] NAME_LIST;
}