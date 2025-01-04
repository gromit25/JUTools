package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

import lombok.Getter;
import lombok.Setter;

/**
 * 명령어 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class Instruction {
	
	/** 다음 실행 명령어 */
	@Getter
	@Setter
	private Instruction next;
	
	/**
	 * 명령어 수행 메소드
	 * 
	 * @param stack 스택
	 * @param values 메모리
	 */
	public abstract int execute(Stack<Object> stack, Map<String, ?> values) throws Exception;
}
