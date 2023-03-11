package com.jutools.instructions;

import java.util.Map;
import java.util.Stack;

/**
 * 
 * 
 * @author jmsohn
 */
public class LOAD_FALSE extends Instruction {

	@Override
	public void execute(Stack<Object> stack, Map<String, Object> values) throws Exception {
		stack.push(Boolean.FALSE);
	}

}
