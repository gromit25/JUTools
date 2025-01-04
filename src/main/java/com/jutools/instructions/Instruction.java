package com.jutools.instructions;

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
}
