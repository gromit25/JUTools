package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * False 값을 스택에 넣는 명령어 클래스
 * 
 * @author jmsohn
 */
public class LOAD_FALSE extends Instruction {

	@Override
	public void execute(Stack<Object> stack, Map<String, ?> values) throws Exception {
		stack.push(Boolean.FALSE);
	}

}
