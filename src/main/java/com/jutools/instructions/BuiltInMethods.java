package com.jutools.instructions;

public class BuiltInMethods {
	
	/**
	 * 반올림 메소드
	 * 
	 * @param value 반올림 대상
	 * @return 반올림 수행 결과
	 */
	@MethodAlias(alias = "round")
	public static double round(double value) {
		return Math.round(value);
	}
	
	/**
	 * 지수 곱 메소드
	 * 
	 * @param base 지수 곱을 할 base 값
	 * @param exponent 지수 곱 횟수
	 * @return 지수 곱 결과
	 */
	@MethodAlias(alias = "pow")
	public static double pow(double base, double exponent) {
		return Math.pow(base, exponent);
	}
}
