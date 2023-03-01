package com.jutools;

import com.jutools.env.Env;

public class Config {
	@Env(name = "config.name", mandatory = true)
	public static String NAME;
}