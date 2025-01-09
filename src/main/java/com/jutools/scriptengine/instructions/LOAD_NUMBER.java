package com.jutools.scriptengine.instructions;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;

/**
 * 스택에 숫자(double 형) 추가
 * 
 * @author jmsohn
 */
public class LOAD_NUMBER extends Instruction {
	
	/** 설정할 숫자 */
	@Getter
	private double value;
	
	/**
	 * 생성자
	 * 
	 * @param value 설정할 숫자
	 */
	public LOAD_NUMBER(double value) {
		this.value = value;
	}

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 스택에 숫자 추가
		stack.push(this.value);
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}

	@Override
	protected String getParamString() {
		
		StringBuilder paramString = new StringBuilder("");
		
		paramString
			.append(this.value);
		
		return paramString.toString();
	}
}
