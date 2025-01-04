package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 스택의 값에 따라 실행 위치 변경<br>
 * 값이 False 이면, 파라미터 1 만큼 이동,<br>
 * True 이면, 파라미터 2 만큼 이동
 * 
 * @author jmsohn
 */
public class IF_FALSE extends Instruction {
	
	/** 파라미터 1(이동할 거리) */
	@Getter
	@Setter
	private int p1;
	
	/** 파라미터 2(이동할 거리) */
	@Getter
	@Setter
	private int p2;

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 파라미터를 스택에서 꺼내옴
		Object value = stack.pop();
		
		// value 에 대한 검증 수행
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		// 스택의 값이 False 이면 파라미터 1 만큼 이동하도록 반환
		// 만일 True 이면 파라미터 2 만큼 이동하도록 반환
		if(Boolean.FALSE.equals(value) == true) {
			return this.p1;
		} else {
			return this.p2;
		}
	}
}
