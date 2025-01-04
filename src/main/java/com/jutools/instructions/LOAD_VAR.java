package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 스택에 변수 값 추가
 * 
 * @author jmsohn
 */
public class LOAD_VAR extends Instruction {

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		Object value = values.get(this.getParam(0));
		
		if(value == null) {
			
			// 변수 값이 null 일 경우, null 추가
			stack.push(null);
			
		} else {
			
			Class<?> valueClass = value.getClass();
			
			if(valueClass == int.class || valueClass == Integer.class
				|| valueClass == long.class || valueClass == Long.class
				|| valueClass == float.class || valueClass == Float.class) {
				
				// 숫자형 데이터는 double 형으로 추가
				stack.push(((Number)value).doubleValue());
				
			} else {
				
				// 숫자형이 아닐 경우, 값 자체로 추가
				stack.push(value);
			}
		}
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
}
