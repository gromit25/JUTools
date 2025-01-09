package com.jutools.scriptengine;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 컬렉션에서 하나의 요소를 스택에 추가
 * 
 * @author jmsohn
 */
public class LOAD_ELEMENT extends Instruction {

	@SuppressWarnings("rawtypes")
	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 객체의 인덱스를 스택에서 가져옴
		Object index = stack.pop();
		if(index == null) {
			throw new NullPointerException("stack have no object.");
		}
		
		// 속성을 가져올 객체를 스택에서 가져옴
		Object obj = stack.pop();
		if(obj == null) {
			throw new NullPointerException("stack have no object.");
		}
		
		// 콜렉션 객체의 element 변수
		Object element = null;
		
		// List, Map 타입 별로 element를 가져옴
		if(List.class.isAssignableFrom(obj.getClass()) == true) {
			element = ((List)obj).get(toInt(index));
		} else if(Map.class.isAssignableFrom(obj.getClass()) == true) {
			element = ((Map)obj).get(index);
		} else {
			throw new Exception("unexpected type:" + obj.getClass().getCanonicalName());
		}
		
		// 속성값을 스택에 푸시
		stack.push(element);
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
	
	/**
	 * 주어진 대상 객체 를 int 값으로 변환 
	 * 
	 * @param obj 대상 객체
	 * @return 변환된 int 값
	 */
	private static int toInt(Object obj) throws Exception {
		
		if(
			obj.getClass() == int.class || obj instanceof Integer
		) {
			return (int)obj;
		} else if(obj.getClass() == long.class || obj instanceof Long) {
			return ((Long)obj).intValue();
		} else if(obj.getClass() == float.class || obj instanceof Float) {
			return ((Float)obj).intValue();
		} else if(obj.getClass() == double.class || obj instanceof Double) {
			return ((Double)obj).intValue();
		} else {
			throw new Exception("unexpected type: " + obj.getClass());
		}
	}
}
