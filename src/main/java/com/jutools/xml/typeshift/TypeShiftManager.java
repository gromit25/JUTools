package com.jutools.xml.typeshift;

import java.util.HashMap;
import java.util.Map;

import com.jutools.StringUtil;

/**
 * 타입 변환 관리자 클래스
 * 
 * @author jmsohn
 */
public class TypeShiftManager {
	
	/** 타입 변환 맵 - Key: 변환 타입명, Value: 변환 객체 */
	private static Map<String, TypeShift> typeShifts = new HashMap<>();
	
	static {
		// 디폴트 타입 변환 객체 추가
		typeShifts.put("String", new StringTypeShift());
		typeShifts.put("Int", new IntTypeShift());
		typeShifts.put("Long", new LongTypeShift());
		typeShifts.put("Float", new FloatTypeShift());
		typeShifts.put("Double", new DoubleTypeShift());
	}
	
	/**
	 * 타입 변환 객체 등록 메소드
	 * 
	 * @param name 변환 타입명
	 * @param typeShift 변환 객체
	 */
	public static void registTypeShift(String name, TypeShift typeShift) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(name) == true) {
			throw new IllegalArgumentException("name is blank or null.");
		}
		
		if(typeShift == null) {
			throw new IllegalArgumentException("type shift obj is null.");
		}
		
		// type shift 등록
		typeShifts.put(name, typeShift);
	}
	
	/**
	 * 변환 타입명에 해당하는 타입 변환 객체 반환
	 * 
	 * @param name 변환 타입명
	 * @return 변환 타입명에 해당하는 타입 변환 객체
	 */
	public static TypeShift getTypeShift(String name) {
		
		// 변환 타입명의 타입 변환 객체를 가져옴
		TypeShift typeShift = typeShifts.get(name);
		
		// 타입 변환 객체를 반환
		// 만일 없을 경우 디폴트로 String 변환 객체를 반환
		if(typeShift != null) {
			return typeShift;
		} else {
			return typeShifts.get("String");
		}
	}
}
