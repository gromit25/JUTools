package com.jutools.mathexp;

/**
 * 
 * 
 * @author jmsohn
 */
public class BuiltInMethods {
	
	@MethodMap(alias = "round")
	public static double round(double value) {
		return Math.round(value);
	}
	
	@MethodMap(alias = "pow")
	public static double pow(double base, double exponent) {
		return Math.pow(base, exponent);
	}

}
