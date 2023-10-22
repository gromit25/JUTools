package com.jutools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Function;

/**
 * type(class)와 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class TypeUtil {
	
	/**
	 * int 값을 byte 목록(8바이트)으로 변환
	 * 
	 * @param value 변환할 int 값
	 * @return 변환된 byte 목록
	 */
	public static byte[] intToBytes(long value) {
		
	    byte[] bytes = new byte[4];
	    
	    for (int i = 0; i < 4; i++) {
	        bytes[3 - i] = (byte) (value >> (i * 8));
	    }
	    
	    return bytes;
	}
	
	/**
	 * long 값을 byte 목록(8바이트)으로 변환
	 * 
	 * @param value 변환할 long 값
	 * @return 변환된 byte 목록
	 */
	public static byte[] longToBytes(long value) {
		
	    byte[] bytes = new byte[8];
	    
	    for (int i = 0; i < 8; i++) {
	        bytes[7 - i] = (byte) (value >> (i * 8));
	    }
	    
	    return bytes;
	}
	
	/**
	 * float 값을 byte 목록(4바이트)으로 변환
	 * 
	 * @param value 변환할 float 값
	 * @return 변환된 byte 목록
	 */
	public static byte[] floatToBytes(float value) {
		
		int intBits = Float.floatToIntBits(value);
	    
		byte[] bytes = new byte[4];
	    for (int i = 0; i < 4; i++) {
	        bytes[3 - i] = (byte) (intBits >> (i * 8));
	    }
	    
	    return bytes;
	}
	
	/**
	 * double 값을 byte 목록(8바이트)으로 변환
	 * 
	 * @param value 변환할 double 값
	 * @return 변환된 byte 목록
	 */
	public static byte[] doubleToBytes(double value) {
		
	    long longBits = Double.doubleToLongBits(value);
	    
	    byte[] bytes = new byte[8];
	    for (int i = 0; i < 8; i++) {
	        bytes[7 - i] = (byte) (longBits >> (i * 8));
	    }
	    
	    return bytes;
	}
	
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
	
	/**
	 * Integer List를 int 배열로 변환함 
	 * 
	 * @param arrayList Integer 타입의 List
	 * @return 변환된 int 배열
	 */
	public static int[] toIntArray(List<Integer> arrayList) {
		
		if(arrayList == null) {
			return new int[0];
		}
		
		int[] intArray = new int[arrayList.size()];
		for(int index = 0; index < intArray.length; index++) {
			intArray[index] = arrayList.get(index);
		}
		
		return intArray;
		
	}
	
	/**
	 * Float List를 float 배열로 변환함 
	 * 
	 * @param arrayList Float 타입의 List
	 * @return 변환된 float 배열
	 */
	public static float[] toFloatArray(List<Float> arrayList) {
		
		if(arrayList == null) {
			return new float[0];
		}
		
		float[] floatArray = new float[arrayList.size()];
		for(int index = 0; index < floatArray.length; index++) {
			floatArray[index] = arrayList.get(index);
		}
		
		return floatArray;
		
	}
	
	/**
	 * List 형 객체를 배열 형태로 변환하여 반환<br>
	 * List 형 객체가 null 일 경우, 빈 배열이 반환됨(null 반환 아님)
	 * 
	 * @param array 변환할 List 객체
	 * @param elementType List 객체 요소의 타입
	 * @return 변환된 배열 객체
	 */
	public static <T> T[] toArray(List<T> arrayList, Class<T> elementType) throws Exception {
		
		// 리스트의 요소 타입이 정의 되지 않은 경우 예외 발생
		if(elementType == null) {
			throw new Exception("type is null.");
		}
		
		// 생성할 배열 크기 변수
		int arrayLength = 0;
		
		// 리스트가 null일 경우, 빈 배열 생성
		if(arrayList != null) {
			arrayLength = arrayList.size();
		}
		
		// 배열 객체 생성
		// new T[];는 안됨
		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(elementType, arrayLength);
		
		// 배열 복사
		for(int index = 0; index < arrayLength; index++) {
			array[index] = arrayList.get(index);
		}
		
		// 배열 반환
		return array;
	}
	
	/**
	 * 객체의 필드에 값을 설정<br>
	 * private, protected 필드에 값을 설정할 수 있도록 함<br>
	 * 이 메소드를 사용할 경우 illegal access warning이 발생함<br>
	 * 이는 java 실행시 "--illegal-access=warn" 옵션을 추가하여 방지할 수 있음
	 * 
	 * @param obj 객체
	 * @param fieldName 설정할 필드명
	 * @param value 필드에 설정할 값
	 */
	public static void setField(Object obj, String fieldName, Object value) throws Exception {
		
		// 입력값 검증
		if(obj == null) {
			throw new NullPointerException("obj is null");
		}
		
		if(StringUtil.isEmpty(fieldName) == true) {
			throw new NullPointerException("fieldName is empty");
		}
		
		// field 정보를 가져옴
		Class<?> objClass = obj.getClass();
		Field field = objClass.getDeclaredField(fieldName);
		if(field == null) {
			throw new Exception(fieldName + " is not found in " + objClass);
		}
		
		// 접근 가능하도록 수정
		int modifier = field.getModifiers();
		if(Modifier.isPublic(modifier) == false || Modifier.isFinal(modifier) == true) { 
			field.setAccessible(true);
		}
		
		// 값을 설정함
		field.set(obj, value);
	}
	
	/**
	 * 자바 기본 데이터 타입 여부 반환<br>
	 * Wrapper 클래스도 기본 데이터 타입으로 간주함<br>
	 * ex) Integer, Boolean 등도 기본 데이터 타입으로 반환함<br>
	 * 만일, 검사할 데이터 타입(type)이 null이면, false를 반환함
	 * 
	 * @param type 검사할 데이터 타입
	 * @return 기본 데이터 타입 여부
	 */
	public static boolean isPrimitive(Class<?> type) {
		
		// 타입이 null 이면 false 반환
		if(type == null) {
			return false;
		}
		
		// 기본 데이터 타입 여부를 검사하여 반환
		if(type == boolean.class || type == Boolean.class
			|| type == byte.class || type == Byte.class
			|| type == char.class || type == Character.class
			|| type == short.class || type == Short.class
			|| type == int.class || type == Integer.class
			|| type == long.class || type == Long.class
			|| type == float.class || type == Float.class
			|| type == double.class || type == Double.class) {
			
			return true;
			
		} else {
			
			return false;
		}
		
	}
	
	/**
	 * 자바 기본형 타입의 바이트 크기를 반환 
	 * 
	 * @param type 자바 기본형 타입
	 * @return 자바 기본형 타입의 바이트 크기
	 */
	public static int getPrimitiveSize(Class<?> type) throws Exception {
		
		// 입력값이 자바 기본형인지 검사
		if(isPrimitive(type) == false) {
			throw new IllegalArgumentException("type is not primitive:" + type);
		}
		
		// 각 타입별 크기 반환
		if(type == byte.class || type == Byte.class) {
			return 1;
		} else if(type == byte.class || type == Byte.class) {
			return 1;
		} else if(type == char.class || type == Character.class) {
			return 2;
		} else if(type == short.class || type == Short.class) {
			return 2;
		} else if(type == int.class || type == Integer.class) {
			return 4;
		} else if(type == long.class || type == Long.class) {
			return 8;
		} else if(type == float.class || type == Float.class) {
			return 4;
		} else if(type == double.class || type == Double.class) {
			return 8;
		} else {
			throw new Exception("type is not primitive type:" + type);
		}
	}
	
	/**
	 * 클래스 속성의 public getter 메소드 반환<br>
	 * 없을 경우 null 을 반환
	 * 
	 * @param clazz 클래스
	 * @param fieldName 속성명
	 * @return getter 메소드 객체
	 */
	public Method getGetter(Class<?> clazz, String fieldName) throws Exception {
		
		// 입력값 검사
		if(clazz == null) {
			throw new NullPointerException("class is null.");
		}
		
		if(StringUtil.isEmpty(fieldName) == true) {
			throw new IllegalArgumentException("field name is empty:" + fieldName);
		}
		
		// 클래스에 속성 정보 획득
		// 주의) getField 메소드를 사용하면 안됨
		//      getField 메소드는 public만 가져옴
		Field field = clazz.getDeclaredField(fieldName);
		if(field == null) {
			throw new IllegalArgumentException(fieldName + " is not found in " + clazz.getCanonicalName());
		}
		
		// field의 타입이 boolean 이면 prefix가 "is", 아닐 경우 "get"
		String prefix = (field.getType() == Boolean.class || field.getType() == boolean.class)?"is":"get";
		
		// getter 메소드 명 생성
		String getterName = prefix + Character.toUpperCase(fieldName.charAt(0));
		if(fieldName.length() > 1) {
			getterName += fieldName.substring(1, fieldName.length());
		}
		
		// 클래스에서 public getter 메소드 획득
		Method getter = clazz.getMethod(getterName);
		
		// public getter 메소드 반환
		return getter;
	}
	
	/**
	 * 클래스 속성의 public setter 메소드 반환<br>
	 * 없을 경우 null 을 반환
	 * 
	 * @param clazz 클래스
	 * @param fieldName 속성명
	 * @return setter 메소드 객체
	 */
	public Method getSetter(Class<?> clazz, String fieldName) throws Exception {
		
		// 입력값 검사
		if(clazz == null) {
			throw new NullPointerException("class is null.");
		}
		
		if(StringUtil.isEmpty(fieldName) == true) {
			throw new IllegalArgumentException("field name is empty:" + fieldName);
		}
		
		// 클래스에 속성 정보 획득
		// 주의) getField 메소드를 사용하면 안됨
		//      getField 메소드는 public만 가져옴
		Field field = clazz.getDeclaredField(fieldName);
		if(field == null) {
			throw new IllegalArgumentException(fieldName + " is not found in " + clazz.getCanonicalName());
		}
		
		// setter 메소드 명 생성
		String setterName = "set" + Character.toUpperCase(fieldName.charAt(0));
		if(fieldName.length() > 1) {
			setterName += fieldName.substring(1, fieldName.length());
		}
		
		// 클래스에서 public setter 메소드 획득
		Method setter = clazz.getMethod(setterName, field.getClass());
		
		// public setter 메소드 반환
		return setter;
	}
}
