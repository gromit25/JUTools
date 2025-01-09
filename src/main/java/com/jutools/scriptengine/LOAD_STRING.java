package com.jutools.scriptengine;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;

/**
 * 스택에 문자열 추가
 * 
 * @author jmsohn
 */
public class LOAD_STRING extends Instruction {
	
	/** 설정할 문자열 */
	@Getter
	private String value;

	/**
	 * 생성자
	 * 
	 * @param value 설정할 문자열
	 */
	public LOAD_STRING(String value) {
		this.value = value;
	}

	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 스택에 파라미터로 설정된 문자열 추가
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
