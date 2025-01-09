package com.jutools.scriptengine.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 명령어 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class Instruction {
	
	/**
	 * 명령어 수행 메소드
	 * 
	 * @param stack 스택
	 * @param values 메모리
	 */
	public abstract int execute(Stack<Object> stack, Map<String, ?> values) throws Exception;
	
	/**
	 * 파라미터 문자열 반환
	 * 
	 * @return 파라미터 문자열
	 */
	protected String getParamString() {
		return "";
	}
	
	@Override
	public String toString() {
		
		StringBuilder toString = new StringBuilder("");
		
		toString
			.append(this.getClass().getSimpleName())
			.append("\t")
			.append(this.getParamString());
		
		return toString.toString();
	}
}
