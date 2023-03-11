package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 나눗셈 명령어 클래스
 * 
 * @author jmsohn
 */
public class DIV extends Instruction {

	@Override
	public void execute(Stack<Object> stack, Map<String, Object> values) throws Exception {
		double p2 = (double)stack.pop();
		double p1 = (double)stack.pop();
		
		stack.push(p1 / p2);
	}

}
