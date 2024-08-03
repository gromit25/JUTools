package com.jutools.xml;

import java.util.HashMap;
import java.util.Map;

import com.jutools.StringUtil;

/**
 * 
 * 
 * @author jmsohn
 */
public class TypeShiftManager {
	
	/** */
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
	 * 
	 * @param name
	 * @param typeShift
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
	 * 
	 * @param name
	 * @return
	 */
	static TypeShift getTypeShift(String name) {
		
		//
		TypeShift typeShift = typeShifts.get(name);
		
		//
		if(typeShift != null) {
			return typeShift;
		} else {
			return typeShifts.get("String");
		}
	}
}
