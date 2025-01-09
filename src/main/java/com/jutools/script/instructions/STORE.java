package com.jutools.script.instructions;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;

/**
 * 스택의 값을 변수명으로 values 에 저장<br>
 * 스택 -> values
 * 
 * @author jmsohn
 */
public class STORE extends Instruction {
	
	/** values 에 추가할 변수명 */
	@Getter
	private String name;
	
	/**
	 * 생성자
	 * 
	 * @param name 변수명
	 */
	public STORE(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		
		// 스택 크기 확인
		if(stack.size() == 0) {
			throw new NullPointerException("stack is empty.");
		}
		
		// 스택에서 값 획득
		Object value = stack.pop();
		
		// values 에 넣음
		((Map<String, Object>)values).put(this.name, value);
		
		// 다음 실행 명령어 이동 거리 반환
		return 1;
	}
	
	@Override
	protected String getParamString() {
		
		StringBuilder paramString = new StringBuilder("");
		
		paramString
			.append(this.name);
		
		return paramString.toString();
	}
}
