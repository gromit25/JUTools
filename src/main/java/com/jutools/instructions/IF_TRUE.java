package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 스택의 값에 따라 실행 위치 변경<br>
 * 값이 True 이면, 파라미터 1 만큼 이동,<br>
 * False 이면, 파라미터 2 만큼 이동
 * 
 * @author jmsohn
 */
public class IF_TRUE extends Instruction {
	
	/** 파라미터 1(true 일때 이동할 거리) */
	@Getter
	@Setter
	private int p1;
	
	/** 파라미터 2(false 일때, 이동할 거리) */
	@Getter
	@Setter
	private int p2;
	
	/**
	 * 생성자
	 * 
	 * @param p1 파라미터 1(true 일때 이동할 거리)
	 * @param p2 파라미터 2(false 일때, 이동할 거리)
	 */
	public IF_TRUE(int p1, int p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 파라미터를 스택에서 꺼내옴
		Object value = stack.pop();
		
		// value 에 대한 검증 수행
		if(value == null) {
			throw new NullPointerException("value is null.");
		}
		
		// 스택의 값이 True 이면 파라미터 1 만큼 이동하도록 반환
		// 만일 False 이면 파라미터 2 만큼 이동하도록 반환
		if(Boolean.TRUE.equals(value) == true) {
			return this.p1;
		} else {
			return this.p2;
		}
	}
	
	@Override
	protected String getParamString() {
		
		StringBuilder paramString = new StringBuilder("");
		
		paramString
			.append(this.p1)
			.append(",")
			.append(this.p2);
		
		return paramString.toString();
	}
}
