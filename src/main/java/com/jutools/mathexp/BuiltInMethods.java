package com.jutools.mathexp;

public class BuiltInMethods {
	
	@MethodMap(alias = "round")
	public static double round(double value) {
		return Math.round(value);
	}

}
