package com.jutools;

import java.util.function.Function;

/**
 * type(class)와 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class TypeUtil {
	
	/**
	 * obj의 타입에 따라 parsing 및 변환을 수행하여 반환하는 메소드
	 * 
	 * @param <T> 변환할 obj의 타입
	 * @param obj 객체
	 * @param type 변환할 obj의 타입의 클래스
	 * @param parser 문자열일 경우 파싱하는 메소드(람다 함수)
	 * @return parsing 및 변환 완료된 값
	 */
	private static <T> T toXXX(Object obj, Class<T> type, Function<String, T> parser) throws Exception {
		
		// 입력 값 검증
		if(obj == null) {
			throw new NullPointerException("obj is null");
		}
		
		if(type == null) {
			throw new NullPointerException("type is null");
		}
		
		if(parser == null) {
			throw new NullPointerException("parser is null");
		}
		
		// 변환할 obj 타입에 따라 변환 작업 수행
		if(type.isAssignableFrom(obj.getClass()) == true) {
			return type.cast(obj);
		} else if(obj instanceof String) {
			return parser.apply(obj.toString());
		} else {
			throw new Exception("Unexpected type:" + obj.getClass());
		}
	}
	
	/**
	 * obj를 boolean으로 변환함<br>
	 * -> obj는 boolean, Boolean, boolean의 문자열 중 하나이어야 함
	 * 
	 * @param obj 변환할 obj
	 * @return 변환된 boolean 값
	 */
	public static boolean toBoolean(Object obj) throws Exception {
		return toXXX(obj, Boolean.class, str -> {
			return Boolean.parseBoolean(str);
		});
	}

	/**
	 * obj를 int로 변환함<br>
	 * -> obj는 int, Integer, int의 문자열 중 하나이어야 함
	 * 
	 * @param obj 변환할 obj
	 * @return 변환된 int 값
	 */
	public static int toInteger(Object obj) throws Exception {
		return toXXX(obj, Integer.class, str -> {
			return Integer.parseInt(str);
		});
	}

	/**
	 * obj를 long으로 변환함<br>
	 * -> obj는 long, Long, long의 문자열 중 하나이어야 함
	 * 
	 * @param obj 변환할 obj
	 * @return 변환된 long 값
	 */
	public static long toLong(Object obj) throws Exception {
		return toXXX(obj, Long.class, str -> {
			return Long.parseLong(str);
		});
	}
	
	/**
	 * obj를 float로 변환함<br>
	 * -> obj는 float, Float, float의 문자열 중 하나이어야 함
	 * 
	 * @param obj 변환할 obj
	 * @return 변환된 float 값
	 */
	public static float toFloat(Object obj) throws Exception {
		return toXXX(obj, Float.class, str -> {
			return Float.parseFloat(str);
		});
	}

	/**
	 * obj를 double로 변환함<br>
	 * -> obj는 double, Double, double의 문자열 중 하나이어야 함
	 * 
	 * @param obj 변환할 obj
	 * @return 변환된 double 값
	 */
	public static double toDouble(Object obj) throws Exception {
		return toXXX(obj, Double.class, str -> {
			return Double.parseDouble(str);
		});
	}
}
